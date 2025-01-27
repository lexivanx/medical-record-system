package com.med.recordsystem.repository;

import com.med.recordsystem.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    Diagnosis findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT u.fullName FROM User u JOIN UserDiagnosis ud ON u.id = ud.id.userId WHERE ud.id.diagnosisId = :diagnosisId")
    List<List<Object>> findPatientsWithDiagnosis(@Param("diagnosisId") Long diagnosisId);

    @Query("SELECT d.name, COUNT(ud.id.userId) FROM Diagnosis d LEFT JOIN UserDiagnosis ud ON d.id = ud.id.diagnosisId GROUP BY d.name")
    List<List<Object>> findPatientCountPerDiagnosis();

}
