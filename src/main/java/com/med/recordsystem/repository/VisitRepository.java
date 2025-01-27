package com.med.recordsystem.repository;

import com.med.recordsystem.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findByPatientId(Long patientId);
    List<Visit> findByDoctorId(Long doctorId);
    List<Visit> findBySlot(LocalDateTime slot);

    @Modifying
    @Transactional
    @Query("DELETE FROM Visit v WHERE v.doctor.id = :doctorId")
    void deleteByDoctorId(@Param("doctorId") Long doctorId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Visit v WHERE v.patient.id = :patientId")
    void deleteByPatientId(@Param("patientId") Long patientId);

    boolean existsByDoctorIdAndPatientId(Long doctorId, Long patientId);

    @Query("SELECT d.fullName, COUNT(v.id) FROM Visit v JOIN User d ON v.doctor.id = d.id GROUP BY d.fullName")
    List<List<Object>> findVisitCountPerDoctor();

    @Query("SELECT v.slot, v.treatment, v.doctor.fullName FROM Visit v WHERE v.patient.id = :patientId")
    List<List<Object>> findVisitsByPatient(@Param("patientId") Long patientId);


    @Query("SELECT v.doctor.fullName, v.slot, v.patient.fullName FROM Visit v WHERE v.slot >= :startDateTime AND v.slot <= :endDateTime")
    List<List<Object>> findVisitsInPeriod(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);


    @Query("SELECT v.slot, p.fullName FROM Visit v JOIN User p ON v.patient.id = p.id WHERE v.doctor.id = :doctorId AND v.slot >= :startDateTime AND v.slot <= :endDateTime")
    List<List<Object>> findVisitsByDoctorInPeriod(@Param("doctorId") Long doctorId,
                                                  @Param("startDateTime") LocalDateTime startDateTime,
                                                  @Param("endDateTime") LocalDateTime endDateTime);

}
