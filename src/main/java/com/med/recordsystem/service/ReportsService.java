package com.med.recordsystem.service;

import com.med.recordsystem.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Month;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
public class ReportsService {

    private final UserRepository userRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final VisitRepository visitRepository;
    private final SickLeaveRepository sickLeaveRepository;
    private final PersonalDocsRepository personalDocsRepository;

    public ReportsService(UserRepository userRepository,
                          DiagnosisRepository diagnosisRepository,
                          VisitRepository visitRepository,
                          SickLeaveRepository sickLeaveRepository,
                          PersonalDocsRepository personalDocsRepository) {
        this.userRepository = userRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.visitRepository = visitRepository;
        this.sickLeaveRepository = sickLeaveRepository;
        this.personalDocsRepository = personalDocsRepository;
    }

    public List<List<Object>> getPatientsWithDiagnosis(Long diagnosisId) {
        return diagnosisRepository.findPatientsWithDiagnosis(diagnosisId);
    }

    public List<List<Object>> getPatientCountPerDiagnosis() {
        return diagnosisRepository.findPatientCountPerDiagnosis();
    }

    public List<List<Object>> getPatientsWithDoctor(Long doctorId) {
        return personalDocsRepository.findPatientsByDoctor(doctorId);
    }

    public List<List<Object>> getPatientCountPerDoctor() {
        return personalDocsRepository.findPatientCountPerDoctor();
    }

    public List<List<Object>> getVisitCountPerDoctor() {
        return visitRepository.findVisitCountPerDoctor();
    }

    public List<List<Object>> getVisitsOfPatient(Long patientId) {
        return visitRepository.findVisitsByPatient(patientId);
    }

    public List<List<Object>> getVisitsInPeriod(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end   = LocalDate.parse(endDate);

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime   = end.atTime(LocalTime.MAX);

        return visitRepository.findVisitsInPeriod(startDateTime, endDateTime);
    }

    public List<List<Object>> getVisitsOfDoctorInPeriod(Long doctorId, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end   = LocalDate.parse(endDate);

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime   = end.atTime(LocalTime.MAX);
        return visitRepository.findVisitsByDoctorInPeriod(doctorId, startDateTime, endDateTime);
    }

    public List<List<Object>> getMostSickLeavesMonth(Integer year) {
        Pageable pageable = PageRequest.of(0, 1);
        List<Object[]> results = sickLeaveRepository.findMonthWithMostSickLeaves(year, pageable);

        if (results.isEmpty()) {
            return Collections.emptyList();
        }

        Object[] topResult = results.get(0);
        Integer monthNumber = (Integer) topResult[0];
        Long sickLeaveCount = ((Number) topResult[1]).longValue();

        String monthName = Month.of(monthNumber).name();

        return List.of(List.of(monthName, sickLeaveCount));
    }

    public List<List<Object>> getDoctorWithMostSickLeaves() {
        return sickLeaveRepository.findDoctorWithMostSickLeaves();
    }
}
