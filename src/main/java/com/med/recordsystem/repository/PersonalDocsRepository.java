package com.med.recordsystem.repository;

import com.med.recordsystem.model.PersonalDocs;
import com.med.recordsystem.model.PersonalDocsId;
import com.med.recordsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PersonalDocsRepository extends JpaRepository<PersonalDocs, PersonalDocsId> {
    List<PersonalDocs> findByIdDoctorId(Long doctorId);
    List<PersonalDocs> findByIdPatientId(Long patientId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PersonalDocs pd WHERE pd.doctor.id = :doctorId")
    void deleteByIdDoctorId(@Param("doctorId") Long doctorId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PersonalDocs pd WHERE pd.patient.id = :patientId")
    void deleteByIdPatientId(@Param("patientId") Long patientId);

    @Query("SELECT d FROM User d JOIN PersonalDocs pd ON d.id = pd.doctor.id WHERE pd.patient.id = :patientId")
    Optional<User> findPersonalDoctorByPatientId(@Param("patientId") Long patientId);

    boolean existsByIdDoctorIdAndIdPatientId(Long doctorId, Long patientId);

    boolean existsByIdPatientId(Long patientId);

    @Query("SELECT u.fullName FROM User u JOIN PersonalDocs pd ON u.id = pd.id.patientId WHERE pd.id.doctorId = :doctorId")
    List<List<Object>> findPatientsByDoctor(@Param("doctorId") Long doctorId);

    @Query("SELECT d.fullName, COUNT(pd.id.patientId) FROM User d JOIN PersonalDocs pd ON d.id = pd.id.doctorId GROUP BY d.fullName")
    List<List<Object>> findPatientCountPerDoctor();
}

