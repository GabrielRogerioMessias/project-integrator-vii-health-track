package org.projetointegrador.unifio.projectintegratorviibackend.repositories;

import org.projetointegrador.unifio.projectintegratorviibackend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
