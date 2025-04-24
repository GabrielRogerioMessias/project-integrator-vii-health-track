package org.projetointegrador.unifio.projectintegratorviibackend.repositories;

import org.projetointegrador.unifio.projectintegratorviibackend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    @Query(value = "SELECT P FROM Patient AS P where P.CPF =:cpf")
    Patient findPatientByCPF(@Param("cpf") String cpf);
}
