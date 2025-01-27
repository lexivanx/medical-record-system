package com.med.recordsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_diagnoses")
public class UserDiagnosis {

    @EmbeddedId
    private UserDiagnosisId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @MapsId("diagnosisId")
    @JoinColumn(name = "id_diagnosis")
    private Diagnosis diagnosis;

    public UserDiagnosis() {}

    public UserDiagnosis(User user, Diagnosis diagnosis) {
        this.id = new UserDiagnosisId(user.getId(), diagnosis.getId());
        this.user = user;
        this.diagnosis = diagnosis;
    }

    public UserDiagnosisId getId() {
        return id;
    }

    public void setId(UserDiagnosisId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Long getUserId() {
        return id.getUserId();
    }

    public void setUserId(Long userId) {
        id.setUserId(userId);
    }

    public Long getDiagnosisId() {
        return id.getDiagnosisId();
    }

    public void setDiagnosisId(Long diagnosisId) {
        id.setDiagnosisId(diagnosisId);
    }
}

