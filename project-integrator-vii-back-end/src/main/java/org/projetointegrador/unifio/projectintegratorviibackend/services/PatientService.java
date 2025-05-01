package org.projetointegrador.unifio.projectintegratorviibackend.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.projetointegrador.unifio.projectintegratorviibackend.models.mappers.PatientMapper;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.Patient;
import org.projetointegrador.unifio.projectintegratorviibackend.models.User;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.PatientRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.PatientResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.enums.PermissionEnum;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.PatientUpdateDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.PatientRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.UserRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.security.jwt.EmailTokenUtil;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.NullEntityFieldException;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.UserAlreadyRegistered;
import org.projetointegrador.unifio.projectintegratorviibackend.utils.AuthenticatedUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class PatientService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final Validator validator;
    private final EmailService emailService;
    private final PatientMapper patientMapper;
    private final AuthenticatedUser authenticatedUser;

    public PatientService(UserRepository userRepository, PatientRepository patientRepository, Validator validator, EmailService emailService, AuthenticatedUser authenticatedUser, PatientMapper patientMapper) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.validator = validator;
        this.emailService = emailService;
        this.authenticatedUser = authenticatedUser;
        this.patientMapper = patientMapper;
    }

    public PatientResponseDTO registerPatient(PatientRegistrationDTO registerData) {
        User userVerify = userRepository.findByEmail(registerData.getEmail());
        if (userVerify != null) {
            if (userVerify.isVerified()) {
                throw new UserAlreadyRegistered(registerData.getEmail(), "already registered and verified!");
            } else {
                String verificationToken = EmailTokenUtil.generateValidationToken(userVerify.getEmail());
                userVerify.setVerificationToken(verificationToken);
                emailService.sendVerificationEmail(userVerify.getEmail(), verificationToken);
                userRepository.save(userVerify);
            }
        }
        Patient patient = patientRepository.findPatientByCPF(registerData.getCPF());
        if (patient != null) {
            throw new UserAlreadyRegistered(patient.getCPF());
        }
        patient = new Patient();
        User user = new User();

        List<String> errors;
        errors = validFields(registerData);
        if (!errors.isEmpty()) {
            throw new NullEntityFieldException(errors);
        }
        //generate a token verify
        String verificationToken = EmailTokenUtil.generateValidationToken(registerData.getEmail());
        user.setVerificationToken(verificationToken);
        // creating a patient with registerData
        patient.setName(registerData.getName());
        patient.setCPF(registerData.getCPF());
        patient.setBirth(registerData.getBirth());
        patient.setWeight(registerData.getWeight());
        patient.setPhone(registerData.getPhone());
        patient.setCreatedAt(new Date());
        // creating a user with register data
        List<PermissionEnum> permissions = new ArrayList<>();
        permissions.add(PermissionEnum.PATIENT);

        user.setPermissions(permissions);
        user.setEnabled(true);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        user.setEmail(registerData.getEmail());
        user.setPassword(this.encoderPassword().encode(registerData.getPassword()));
        user.setPatient(patient);
        patient.setUser(user);
        // persisting a new user in database, and a new patient
        emailService.sendVerificationEmail(registerData.getEmail(), verificationToken);
        userRepository.save(user);
        return patientMapper.toResponseDTO(patient);
    }

    public PatientResponseDTO updatePatient(PatientUpdateDTO uptPatient) {
        Patient authPatient = authenticatedUser.getCurrentUser().getPatient();
        List<String> errors;
        errors = this.validFields(uptPatient);
        if (!errors.isEmpty()) {
            throw new NullEntityFieldException(errors);
        }
        this.updateFields(authPatient, uptPatient);
        Patient uptP = patientRepository.save(authPatient);
        return patientMapper.toResponseDTO(uptP);
    }

    private void updateFields(Patient oldPatient, PatientUpdateDTO uptPatient) {
        if (uptPatient.getPhone() != null && !uptPatient.getName().isBlank()) {
            oldPatient.setName(uptPatient.getName());
        }
        if (!(uptPatient.getBirth() == null)) {
            oldPatient.setBirth(uptPatient.getBirth());
        }
        if (uptPatient.getCPF() != null) {
            oldPatient.setCPF(uptPatient.getCPF());
        }
        if (uptPatient.getPhone() != null && !uptPatient.getPhone().isEmpty()) {
            oldPatient.setPhone(uptPatient.getPhone());
        }
        if (!(uptPatient.getWeight() == null)) {
            oldPatient.setWeight(uptPatient.getWeight());
        }
        if (!(uptPatient.getHeight() == null)) {
            oldPatient.setHeight(uptPatient.getHeight());
        }
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patientsList = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOS = new ArrayList<>();
        for (Patient x : patientsList) {
            patientResponseDTOS.add(patientMapper.toResponseDTO(x));
        }
        return patientResponseDTOS;
    }

    private PasswordEncoder encoderPassword() {
        return new BCryptPasswordEncoder();
    }

    private <T> List<String> validFields(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        return violations.stream().map(ConstraintViolation::getMessage).toList();
    }
}
