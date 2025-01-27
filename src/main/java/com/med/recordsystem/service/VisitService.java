package com.med.recordsystem.service;

import com.med.recordsystem.exception.DataNotFoundException;
import com.med.recordsystem.model.User;
import com.med.recordsystem.model.Visit;
import com.med.recordsystem.repository.VisitRepository;
import com.med.recordsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final UserRepository userRepository;

    @Autowired
    public VisitService(VisitRepository visitRepository, UserRepository userRepository) {
        this.visitRepository = visitRepository;
        this.userRepository = userRepository;
    }

    public List<Visit> getAllVisits() {
        List<Visit> visits = visitRepository.findAll();
        visits.forEach(visit -> {
            visit.getDoctor().getFullName();
            visit.getPatient().getFullName();
        });
        return visits;
    }

    public Optional<Visit> getVisitById(Long id) {
        return visitRepository.findById(id);
    }

    public List<Visit> getVisitsByPatientId(Long patientId) {
        return visitRepository.findByPatientId(patientId);
    }

    public List<Visit> getVisitsByDoctorId(Long doctorId) {
        return visitRepository.findByDoctorId(doctorId);
    }

    public List<Visit> getVisitsBySlot(LocalDateTime slot) {
        return visitRepository.findBySlot(slot);
    }

    public Visit updateVisitForAdmin(Long id, Visit visit) {
        Visit existingVisit = visitRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Visit not found"));
        existingVisit.setSlot(visit.getSlot());
        existingVisit.setTreatment(visit.getTreatment());
        User patient = userRepository.findById(visit.getPatient().getId())
                .orElseThrow(() -> new DataNotFoundException("Patient not found"));
        existingVisit.setPatient(patient);
        return visitRepository.save(existingVisit);
    }

    public Visit saveVisit(Visit visit) {
        if (visit.getPatient() == null || visit.getPatient().getId() == null) {
            throw new IllegalArgumentException("Patient must be selected for the visit");
        }

        User patient = userRepository.findById(visit.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        visit.setPatient(patient);

        if (visit.getDoctor() == null || visit.getDoctor().getId() == null) {
            throw new IllegalStateException("Doctor must be set for the visit");
        }

        return visitRepository.save(visit);
    }

    public Visit saveVisitAsAdmin(Visit visit) {
        User patient = userRepository.findById(visit.getPatient().getId())
                .orElseThrow(() -> new DataNotFoundException("Patient not found"));
        User doctor = userRepository.findById(visit.getDoctor().getId())
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));

        visit.setPatient(patient);
        visit.setDoctor(doctor);

        return visitRepository.save(visit);
    }

    public Visit updateVisitAsAdmin(Long id, Visit visit) {
        Visit existingVisit = visitRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Visit not found"));
        User patient = userRepository.findById(visit.getPatient().getId())
                .orElseThrow(() -> new DataNotFoundException("Patient not found"));
        User doctor = userRepository.findById(visit.getDoctor().getId())
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));

        existingVisit.setPatient(patient);
        existingVisit.setDoctor(doctor);
        existingVisit.setSlot(visit.getSlot());
        existingVisit.setTreatment(visit.getTreatment());

        return visitRepository.save(existingVisit);
    }

    public void deleteVisit(Long id) {
        visitRepository.deleteById(id);
    }

    public List<Visit> getVisitsByUsername(String username) {
        User patient = userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        return visitRepository.findByPatientId(patient.getId());
    }
    public void deleteVisitForDoctor(Long visitId, String doctorUsername) {
        User doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new DataNotFoundException("Visit not found"));

        if (visit.getDoctor().equals(doctor)) {
            visitRepository.delete(visit);
        } else {

            throw new AccessDeniedException("Unauthorized action");
        }
    }

    public Visit updateVisitForDoctor(Long visitId, Visit updatedVisit, String doctorUsername) {
        User doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new DataNotFoundException("Visit not found"));

        if (!visit.getDoctor().equals(doctor)) {

            throw new AccessDeniedException("You are not authorized to update this visit");
        }

        if (updatedVisit.getPatient() != null && updatedVisit.getPatient().getId() != null) {
            User patient = userRepository.findById(updatedVisit.getPatient().getId())
                    .orElseThrow(() -> new DataNotFoundException("Patient not found"));
            visit.setPatient(patient);
        }

        visit.setSlot(updatedVisit.getSlot());
        visit.setTreatment(updatedVisit.getTreatment());

        return visitRepository.save(visit);
    }


    public Visit getVisitForDoctor(Long visitId, String doctorUsername) {
        User doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new DataNotFoundException("Visit not found"));

        if (visit.getDoctor().equals(doctor)) {
            return visit;
        }

        throw new AccessDeniedException("You are not authorized to access this visit");
    }


}
