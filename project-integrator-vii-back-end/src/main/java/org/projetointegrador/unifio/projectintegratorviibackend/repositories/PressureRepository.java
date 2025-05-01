package org.projetointegrador.unifio.projectintegratorviibackend.repositories;

import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.Patient;
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.Pressure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PressureRepository extends JpaRepository<Pressure, Integer> {
    @Query(value = "SELECT P FROM Pressure P WHERE P.patient =:patient")
    List<Pressure> listAllPressureOfPatient(@Param(value = "patient") Patient patient);

    @Query(value = "SELECT P FROM Pressure P WHERE P.patient = :patient AND P.id = :idPressure")
    Optional<Pressure> findPressureById(@Param(value = "patient") Patient patient,
                                        @Param(value = "idPressure") Long idPressure);

    @Query(value = "SELECT P FROM Pressure P WHERE P.patient =:patient AND P.measurementTime BETWEEN :initialDate AND :endDate")
    List<Pressure> listPressureByDate(@Param(value = "patient") Patient patient,
                                      @Param(value = "initialDate") LocalDateTime initialDate,
                                      @Param(value = "endDate") LocalDateTime endDate
    );
}
