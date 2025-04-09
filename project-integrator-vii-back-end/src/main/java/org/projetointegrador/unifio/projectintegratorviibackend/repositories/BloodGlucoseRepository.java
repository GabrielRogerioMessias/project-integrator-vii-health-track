package org.projetointegrador.unifio.projectintegratorviibackend.repositories;

import org.projetointegrador.unifio.projectintegratorviibackend.models.BloodGlucose;
import org.projetointegrador.unifio.projectintegratorviibackend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BloodGlucoseRepository extends JpaRepository<BloodGlucose, Integer> {

    @Query(value = "SELECT BG FROM BloodGlucose BG WHERE BG.patient = :patient")
    public List<BloodGlucose> listAllGlucoseOfUser(@Param(value = "patient") Patient patient);
}
