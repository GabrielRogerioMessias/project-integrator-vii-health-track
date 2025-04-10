package org.projetointegrador.unifio.projectintegratorviibackend.repositories;

import org.projetointegrador.unifio.projectintegratorviibackend.models.BloodGlucose;
import org.projetointegrador.unifio.projectintegratorviibackend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BloodGlucoseRepository extends JpaRepository<BloodGlucose, Integer> {

    @Query(value = "SELECT BG FROM BloodGlucose BG WHERE BG.patient = :patient")
    List<BloodGlucose> listAllGlucoseOfPatient(@Param(value = "patient") Patient patient);

    @Query(value = "SELECT BG FROM BloodGlucose BG WHERE BG.id =:idBloodGlucose AND BG.patient = :patient")
    Optional<BloodGlucose> findBloodGlucoseById(@Param(value = "patient") Patient patient,
                                                @Param(value = "idBloodGlucose") Long idBloodGlucose);
}

