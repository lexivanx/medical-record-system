package com.med.recordsystem.repository;

import com.med.recordsystem.model.UserDiagnosis;
import com.med.recordsystem.model.UserDiagnosisId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserDiagnosisRepository extends JpaRepository<UserDiagnosis, UserDiagnosisId> {
    List<UserDiagnosis> findByIdUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserDiagnosis ud WHERE ud.user.id = :userId")
    void deleteByIdUserId(@Param("userId") Long userId);
}
