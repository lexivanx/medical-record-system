package com.med.recordsystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_patient", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "id_doctor", nullable = false)
    private User doctor;

    @Column(nullable = false)
    private String treatment;

    @Column(nullable = false)
    private LocalDateTime slot;


    public Visit() {}

    public Visit(User patient, User doctor, String treatment, LocalDateTime slot) {
        this.patient = patient;
        this.doctor = doctor;
        this.treatment = treatment;
        this.slot = slot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public LocalDateTime getSlot() {
        return slot;
    }

    public void setSlot(LocalDateTime slot) {
        this.slot = slot;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", treatment='" + treatment + '\'' +
                ", slot=" + slot +
                '}';
    }
}
