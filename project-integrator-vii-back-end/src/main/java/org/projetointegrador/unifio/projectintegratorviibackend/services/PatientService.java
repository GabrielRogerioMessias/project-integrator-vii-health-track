package org.projetointegrador.unifio.projectintegratorviibackend.services;

import org.projetointegrador.unifio.projectintegratorviibackend.models.Patient;
import org.projetointegrador.unifio.projectintegratorviibackend.models.User;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.PatientRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.PatientResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.enums.PermissionEnum;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.PatientRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.UserRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.UserAlreadyRegistered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PatientService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;


    public PatientService(UserRepository userRepository, PatientRepository patientRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }

    public PatientResponseDTO registerPatient(PatientRegistrationDTO registerData) {
        User userVerify = userRepository.findByEmail(registerData.getEmail());
        if (userVerify != null) {
            throw new UserAlreadyRegistered(User.class, registerData.getEmail());
        }
        // creating a patient with registerData
        Patient patient = new Patient();
        patient.setName(registerData.getName());
        patient.setBirth(registerData.getBirth());
        patient.setWeight(registerData.getWeight());
        patient.setPhone(registerData.getPhone());
        patient.setCreatedAt(new Date());
        // creating a user with register data
        List<PermissionEnum> permissions = new ArrayList<>();
        permissions.add(PermissionEnum.PATIENT);
        User user = new User();
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
        userRepository.save(user);
        return new PatientResponseDTO(patient);
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patientsList = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOS = new ArrayList<>();
        for (Patient x : patientsList) {
            patientResponseDTOS.add(new PatientResponseDTO(x));
        }
        return patientResponseDTOS;
    }

    private PasswordEncoder encoderPassword() {
        return new BCryptPasswordEncoder();
    }
}
