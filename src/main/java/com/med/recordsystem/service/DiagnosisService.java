package com.med.recordsystem.service;

import com.med.recordsystem.exception.DataNotFoundException;
import com.med.recordsystem.model.Diagnosis;
import com.med.recordsystem.repository.DiagnosisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;

    @Autowired
    public DiagnosisService(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    public List<Diagnosis> getAllDiagnoses() {
        return diagnosisRepository.findAll();
    }

    public Optional<Diagnosis> getDiagnosisById(Long id) {
        return diagnosisRepository.findById(id);
    }

    public Diagnosis getDiagnosisByName(String name) {
        return diagnosisRepository.findByName(name);
    }

    public Diagnosis saveDiagnosis(Diagnosis diagnosis) {
        return diagnosisRepository.save(diagnosis);
    }

    public void deleteDiagnosis(Long id) {
        if (!diagnosisRepository.existsById(id)) {
            throw new DataNotFoundException("Diagnosis not found");
        }
        diagnosisRepository.deleteById(id);
    }

    public Diagnosis createDiagnosis(String name) {
        boolean exists = diagnosisRepository.existsByName(name);
        if (exists) {
            throw new IllegalArgumentException("A diagnosis with this name already exists.");
        }

        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setName(name);
        return diagnosisRepository.save(diagnosis);
    }
}
