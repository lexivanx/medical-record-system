package com.med.recordsystem.repository;

import com.med.recordsystem.model.SickLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SickLeaveRepository extends JpaRepository<SickLeave, Long> {

    List<SickLeave> findByPatientId(Long patientId);
    List<SickLeave> findByDoctorId(Long doctorId);

    @Query("SELECT MONTH(sl.dateStart), COUNT(sl.id) FROM SickLeave sl WHERE YEAR(sl.dateStart) = :year GROUP BY MONTH(sl.dateStart) ORDER BY COUNT(sl.id) DESC")
    List<Object[]> findMonthWithMostSickLeaves(@Param("year") Integer year, Pageable pageable);

    @Query("SELECT d.fullName, COUNT(sl.id) FROM SickLeave sl " +
            "JOIN User d ON sl.doctor.id = d.id " +
            "GROUP BY d.fullName " +
            "ORDER BY COUNT(sl.id) DESC")
    List<List<Object>> findDoctorWithMostSickLeaves();

    @Modifying
    @Transactional
    @Query("DELETE FROM SickLeave sl WHERE sl.doctor.id = :doctorId")
    void deleteByDoctorId(@Param("doctorId") Long doctorId);

    @Modifying
    @Transactional
    @Query("DELETE FROM SickLeave sl WHERE sl.patient.id = :patientId")
    void deleteByPatientId(@Param("patientId") Long patientId);

}
