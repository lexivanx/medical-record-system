package com.med.recordsystem.service;

import com.med.recordsystem.exception.DataNotFoundException;
import com.med.recordsystem.model.SickLeave;
import com.med.recordsystem.model.User;
import com.med.recordsystem.repository.SickLeaveRepository;
import com.med.recordsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SickLeaveService {

    private final SickLeaveRepository sickLeaveRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public SickLeaveService(SickLeaveRepository sickLeaveRepository, UserRepository userRepository,
                            UserService userService) {
        this.sickLeaveRepository = sickLeaveRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<SickLeave> getAllSickLeaves() {
        return sickLeaveRepository.findAll();
    }

    public Optional<SickLeave> getSickLeaveById(Long id) {
        return sickLeaveRepository.findById(id);
    }

    public List<SickLeave> getSickLeavesByPatientId(Long patientId) {
        return sickLeaveRepository.findByPatientId(patientId);
    }

    public List<SickLeave> getSickLeavesByDoctorId(Long doctorId) {
        return sickLeaveRepository.findByDoctorId(doctorId);
    }

    public SickLeave saveSickLeave(SickLeave sickLeave) {
        return sickLeaveRepository.save(sickLeave);
    }

    public void deleteSickLeave(Long id) {
        sickLeaveRepository.deleteById(id);
    }

    public SickLeave updateSickLeaveForAdmin(Long id, SickLeave updatedSickLeave) {
        SickLeave existingSickLeave = sickLeaveRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Sick leave not found"));
        
        User patient = userRepository.findById(updatedSickLeave.getPatient().getId())
                .orElseThrow(() -> new DataNotFoundException("Patient not found"));
        User doctor = userRepository.findById(updatedSickLeave.getDoctor().getId())
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));

        existingSickLeave.setPatient(patient);
        existingSickLeave.setDoctor(doctor);
        existingSickLeave.setDateStart(updatedSickLeave.getDateStart());
        existingSickLeave.setDurationDays(updatedSickLeave.getDurationDays());

        return sickLeaveRepository.save(existingSickLeave);
    }


    public void deleteSickLeaveForDoctor(Long sickLeaveId, String doctorUsername) {
        User doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));

        SickLeave sickLeave = sickLeaveRepository.findById(sickLeaveId)
                .orElseThrow(() -> new DataNotFoundException("Sick leave not found"));

        boolean hasAccess = userService.validateDoctorAccessToPatient(doctor, sickLeave.getPatient().getId());

        if (!hasAccess) {
            throw new AccessDeniedException("Unauthorized action");
        }

        sickLeaveRepository.delete(sickLeave);
    }

    public SickLeave updateSickLeaveForDoctor(Long sickLeaveId, SickLeave updatedSickLeave, String doctorUsername) {
        User doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));

        SickLeave sickLeave = sickLeaveRepository.findById(sickLeaveId)
                .orElseThrow(() -> new DataNotFoundException("Sick leave not found"));

        boolean hasAccess = userService.validateDoctorAccessToPatient(doctor, sickLeave.getPatient().getId());

        if (!hasAccess) {
            throw new AccessDeniedException("You are not authorized to update this sick leave");
        }

        sickLeave.setDateStart(updatedSickLeave.getDateStart());
        sickLeave.setDurationDays(updatedSickLeave.getDurationDays());
        sickLeave.setPatient(updatedSickLeave.getPatient());
        return sickLeaveRepository.save(sickLeave);
    }

    public SickLeave getSickLeaveForDoctor(Long sickLeaveId, String doctorUsername) {
        User doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));

        SickLeave sickLeave = sickLeaveRepository.findById(sickLeaveId)
                .orElseThrow(() -> new DataNotFoundException("Sick leave not found"));

        boolean hasAccess = userService.validateDoctorAccessToPatient(doctor, sickLeave.getPatient().getId());

        if (hasAccess) {
            return sickLeave;
        }

        throw new AccessDeniedException("You are not authorized to access this sick leave");
    }

    public SickLeave saveSickLeave(SickLeave sickLeave, String doctorUsername) {
        User doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));

        if (sickLeave.getPatient() == null || sickLeave.getPatient().getId() == null) {
            throw new IllegalArgumentException("Patient must be selected");
        }

        User patient = userRepository.findById(sickLeave.getPatient().getId())
                .orElseThrow(() -> new DataNotFoundException("Patient not found"));

        sickLeave.setDoctor(doctor);
        sickLeave.setPatient(patient);

        boolean hasAccess = userService.validateDoctorAccessToPatient(doctor, sickLeave.getPatient().getId());
        if (hasAccess) {
            return sickLeaveRepository.save(sickLeave);
        }

        throw new AccessDeniedException("You are not authorized to access this sick leave");

    }

}
