package com.med.recordsystem.service;

import com.med.recordsystem.exception.DataNotFoundException;
import com.med.recordsystem.model.Diagnosis;
import com.med.recordsystem.model.User;
import com.med.recordsystem.model.UserDiagnosis;
import com.med.recordsystem.model.UserDiagnosisId;
import com.med.recordsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDiagnosisService {

    private final UserDiagnosisRepository userDiagnosisRepository;
    private final UserRepository userRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final UserService userService;

    @Autowired
    public UserDiagnosisService(UserDiagnosisRepository userDiagnosisRepository, UserRepository userRepository, DiagnosisRepository diagnosisRepository
    , UserService userService) {
        this.userDiagnosisRepository = userDiagnosisRepository;
        this.userRepository = userRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.userService = userService;
    }

    public List<UserDiagnosis> getAllUserDiagnoses() {
        return userDiagnosisRepository.findAll();
    }

    public Optional<UserDiagnosis> getUserDiagnosisById(UserDiagnosisId id) {
        return userDiagnosisRepository.findById(id);
    }

    public List<UserDiagnosis> getUserDiagnosesByUserId(Long userId) {
        return userDiagnosisRepository.findByIdUserId(userId);
    }

    public List<Diagnosis> getDiagnosesByUserId(Long userId) {
        return userDiagnosisRepository.findByIdUserId(userId).stream()
                .map(UserDiagnosis::getDiagnosis)
                .collect(Collectors.toList());
    }

    public UserDiagnosis saveUserDiagnosis(UserDiagnosis userDiagnosis) {
        return userDiagnosisRepository.save(userDiagnosis);
    }

    public void deleteUserDiagnosis(UserDiagnosisId id) {
        userDiagnosisRepository.deleteById(id);
    }

    public void deleteUserDiagnosisById(String id) {
        UserDiagnosisId userDiagnosisId = UserDiagnosisId.fromString(id);
        userDiagnosisRepository.deleteById(userDiagnosisId);
    }


    public UserDiagnosis createUserDiagnosis(Long patientId, Long diagnosisId) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new DataNotFoundException("Patient not found"));
        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new DataNotFoundException("Diagnosis not found"));

        UserDiagnosis userDiagnosis = new UserDiagnosis(patient, diagnosis);
        return userDiagnosisRepository.save(userDiagnosis);
    }

    public void deleteUserDiagnosisForDoctor(String id, String doctorUsername) {
        User doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));

        UserDiagnosisId userDiagnosisId = UserDiagnosisId.fromString(id);
        UserDiagnosis userDiagnosis = userDiagnosisRepository.findById(userDiagnosisId)
                .orElseThrow(() -> new DataNotFoundException("User diagnosis not found"));

        boolean hasAccess = userService.validateDoctorAccessToPatient(doctor, userDiagnosis.getUser().getId());
        if (hasAccess) {
            userDiagnosisRepository.delete(userDiagnosis);
        } else {
            throw new AccessDeniedException("You are not authorized to delete this user diagnosis");
        }
    }

    public UserDiagnosis updateUserDiagnosisForDoctor(String id, UserDiagnosis updatedDiagnosis, String doctorUsername) {
        User doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));

        UserDiagnosisId userDiagnosisId = UserDiagnosisId.fromString(id);
        UserDiagnosis userDiagnosis = userDiagnosisRepository.findById(userDiagnosisId)
                .orElseThrow(() -> new DataNotFoundException("User diagnosis not found"));

        boolean hasAccess = userService.validateDoctorAccessToPatient(doctor, userDiagnosis.getUser().getId());
        if (hasAccess) {
            userDiagnosis.setDiagnosis(updatedDiagnosis.getDiagnosis());
            return userDiagnosisRepository.save(userDiagnosis);
        } else {
            throw new AccessDeniedException("You are not authorized to update this user diagnosis");
        }
    }

    public UserDiagnosis createUserDiagnosisForDoctor(Long patientId, Long diagnosisId, String doctorUsername) {
        User doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new DataNotFoundException("Patient not found"));

        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new DataNotFoundException("Diagnosis not found"));

        boolean hasAccess = userService.validateDoctorAccessToPatient(doctor, patientId);
        if (hasAccess) {
            UserDiagnosis userDiagnosis = new UserDiagnosis(patient, diagnosis);
            return userDiagnosisRepository.save(userDiagnosis);
        } else {
            throw new AccessDeniedException("You are not authorized to create a diagnosis for this patient");
        }
    }



}
