package com.med.recordsystem.service;

import com.med.recordsystem.exception.DataNotFoundException;
import com.med.recordsystem.model.PersonalDocs;
import com.med.recordsystem.model.PersonalDocsId;
import com.med.recordsystem.model.User;
import com.med.recordsystem.repository.UserRepository;
import com.med.recordsystem.repository.PersonalDocsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonalDocsService {

    private final PersonalDocsRepository personalDocsRepository;
    private final UserRepository userRepository;

    @Autowired
    public PersonalDocsService(PersonalDocsRepository personalDocsRepository,
                               UserRepository userRepository) {
        this.personalDocsRepository = personalDocsRepository;
        this.userRepository = userRepository;
    }

    public List<PersonalDocs> getAllPersonalDocs() {
        return personalDocsRepository.findAll();
    }

    public Optional<PersonalDocs> getPersonalDocsById(PersonalDocsId id) {
        return personalDocsRepository.findById(id);
    }

    public List<PersonalDocs> getPersonalDocsByDoctorId(Long doctorId) {
        return personalDocsRepository.findByIdDoctorId(doctorId);
    }

    public List<PersonalDocs> getPersonalDocsByPatientId(Long patientId) {
        return personalDocsRepository.findByIdPatientId(patientId);
    }

    public List<User> getPatientsByDoctorId(Long doctorId) {
        return personalDocsRepository.findByIdDoctorId(doctorId).stream()
                .map(PersonalDocs::getPatient)
                .collect(Collectors.toList());
    }

    public List<User> getDoctorsByPatientId(Long patientId) {
        return personalDocsRepository.findByIdPatientId(patientId).stream()
                .map(PersonalDocs::getDoctor)
                .collect(Collectors.toList());
    }

    public PersonalDocs savePersonalDocs(PersonalDocs personalDocs) {
        return personalDocsRepository.save(personalDocs);
    }

    public void deletePersonalDocs(PersonalDocsId id) {
        personalDocsRepository.deleteById(id);
    }

    public Optional<User> getPersonalDoctor(Long patientId) {
        return personalDocsRepository.findPersonalDoctorByPatientId(patientId);
    }

    public void createPersonalDoc(Long doctorId, Long patientId) {
        boolean alreadyAssigned = personalDocsRepository.existsByIdPatientId(patientId);
        if (alreadyAssigned) {
            throw new IllegalArgumentException("This patient already has a personal doctor assigned.");
        }

        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new DataNotFoundException("Patient not found"));

        PersonalDocsId id = new PersonalDocsId(doctor.getId(), patient.getId());
        PersonalDocs personalDocs = new PersonalDocs();
        personalDocs.setId(id);
        personalDocs.setDoctor(doctor);
        personalDocs.setPatient(patient);

        personalDocsRepository.save(personalDocs);
    }


    public void deletePersonalDocById(String id) {
        PersonalDocsId personalDocsId = PersonalDocsId.fromString(id);
        personalDocsRepository.deleteById(personalDocsId);
    }

}
