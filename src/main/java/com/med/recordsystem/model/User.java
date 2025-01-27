package com.med.recordsystem.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false) // Explicit column name
    private String fullName;

    @Column(nullable = true)
    private Long egn;

    @Column(name = "is_ensured", nullable = true)
    private Boolean isEnsured;

    @Column(nullable = true)
    private String specialty;

    @OneToMany(mappedBy = "patient")
    private Set<PersonalDocs> personalDoctors;

    @OneToMany(mappedBy = "doctor")
    private Set<PersonalDocs> personalPatients;

    @OneToMany(mappedBy = "patient")
    private Set<Visit> patientVisits;

    @OneToMany(mappedBy = "doctor")
    private Set<Visit> doctorVisits;

    @OneToMany(mappedBy = "patient")
    private Set<SickLeave> patientSickLeaves;

    @OneToMany(mappedBy = "doctor")
    private Set<SickLeave> doctorSickLeaves;

    @ManyToMany
    @JoinTable(
            name = "user_diagnoses",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_diagnosis")
    )
    private Set<Diagnosis> diagnoses;

    public User() {
    }

    public User(String role, String username, String password, String fullName, Long egn, Boolean isEnsured, String specialty) {
        this.role = role;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.egn = egn;
        this.isEnsured = isEnsured;
        this.specialty = specialty;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + this.role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getEgn() {
        return egn;
    }

    public void setEgn(Long egn) {
        this.egn = egn;
    }

    public Boolean getIsEnsured() {
        return isEnsured;
    }

    public void setIsEnsured(Boolean isEnsured) {
        this.isEnsured = isEnsured;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", egn=" + egn +
                ", isEnsured=" + isEnsured +
                ", specialty='" + specialty + '\'' +
                '}';
    }
}
