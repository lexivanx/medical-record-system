package com.med.recordsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "personal_docs")
public class PersonalDocs {

    @EmbeddedId
    private PersonalDocsId id;

    @ManyToOne
    @MapsId("patientId")
    @JoinColumn(name = "id_patient")
    private User patient;

    @ManyToOne
    @MapsId("doctorId")
    @JoinColumn(name = "id_doc")
    private User doctor;

    public PersonalDocs() {}

    public PersonalDocs(User patient, User doctor) {
        this.id = new PersonalDocsId(patient.getId(), doctor.getId());
        this.patient = patient;
        this.doctor = doctor;
    }

    public PersonalDocsId getId() {
        return id;
    }

    public void setId(PersonalDocsId id) {
        this.id = id;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }
}

