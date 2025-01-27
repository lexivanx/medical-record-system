package com.med.recordsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.io.Serializable;

@Embeddable
public class UserDiagnosisId implements Serializable {

    @Column(name = "id_user")
    private Long userId;

    @Column(name = "id_diagnosis")
    private Long diagnosisId;

    public UserDiagnosisId() {}

    public UserDiagnosisId(Long userId, Long diagnosisId) {
        this.userId = userId;
        this.diagnosisId = diagnosisId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Long diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDiagnosisId that = (UserDiagnosisId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(diagnosisId, that.diagnosisId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, diagnosisId);
    }

    public static UserDiagnosisId fromString(String id) {
        if (id == null || !id.contains("_")) {
            throw new IllegalArgumentException("Invalid composite key format: " + id);
        }

        try {
            String[] parts = id.split("_");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid composite key format: " + id);
            }

            Long userId = Long.parseLong(parts[0]);
            Long diagnosisId = Long.parseLong(parts[1]);

            return new UserDiagnosisId(userId, diagnosisId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in composite key: " + id, e);
        }
    }


}
