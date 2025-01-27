package com.med.recordsystem.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sick_leaves")
public class SickLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_patient", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "id_doctor", nullable = false)
    private User doctor;

    @Column(name = "date_start", nullable = false)
    private LocalDate dateStart;

    @Column(name = "duration_days", nullable = false)
    private int durationDays;

    public SickLeave() {}

    public SickLeave(User patient, User doctor, LocalDate dateStart, int durationDays) {
        this.patient = patient;
        this.doctor = doctor;
        this.dateStart = dateStart;
        this.durationDays = durationDays;
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

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    @Override
    public String toString() {
        return "SickLeave{" +
                "id=" + id +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", dateStart=" + dateStart +
                ", durationDays=" + durationDays +
                '}';
    }
}
