package com.med.recordsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.io.Serializable;

@Embeddable
public class PersonalDocsId implements Serializable {

    @Column(name = "id_doc")
    private Long doctorId;

    @Column(name = "id_patient")
    private Long patientId;

    public PersonalDocsId() {}

    public PersonalDocsId(Long doctorId, Long patientId) {
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonalDocsId that = (PersonalDocsId) o;
        return Objects.equals(doctorId, that.doctorId) &&
                Objects.equals(patientId, that.patientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, patientId);
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public static PersonalDocsId fromString(String id) {
        if (id == null || !id.contains("_")) {
            throw new IllegalArgumentException("Invalid composite key format: " + id);
        }

        try {
            String[] parts = id.split("_");
            return new PersonalDocsId(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid composite key format: " + id);
        }
    }

}
