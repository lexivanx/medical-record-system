package com.med.recordsystem.web.view;

import com.med.recordsystem.service.DiagnosisService;
import com.med.recordsystem.service.ReportsService;
import com.med.recordsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/reports")
public class ReportsViewController {

    private final ReportsService reportsService;
    private final UserService userService;
    private final DiagnosisService diagnosisService;

    public ReportsViewController(ReportsService reportsService,
                                 UserService userService,
                                 DiagnosisService diagnosisService) {
        this.reportsService = reportsService;
        this.userService = userService;
        this.diagnosisService = diagnosisService;
    }

    @GetMapping
    public String viewReportsPage(Model model) {
        model.addAttribute("doctors", userService.getDoctors());
        model.addAttribute("patients", userService.getPatients());
        model.addAttribute("diagnoses", diagnosisService.getAllDiagnoses());
        return "reports/reports";
    }

    @PostMapping("/run")
    public String runQuery(@RequestParam String query,
                           @RequestParam(required = false) Long diagnosisId,
                           @RequestParam(required = false) Long doctorId,
                           @RequestParam(required = false) Long patientId,
                           @RequestParam(required = false) String startDate,
                           @RequestParam(required = false) String endDate,
                           @RequestParam(required = false) Integer year,
                           Model model) {

        List<String> headers = null;
        List<List<Object>> results = null;

        try {
            switch (query) {
                case "1":
                    // List all patients with diagnosis
                    if (diagnosisId == null) {
                        model.addAttribute("errorMessage", "Diagnosis is required for this query.");
                        break;
                    }
                    headers = List.of("Patient Name");
                    results = reportsService.getPatientsWithDiagnosis(diagnosisId);
                    break;
                case "2":
                    // Sum patients per diagnosis
                    headers = List.of("Diagnosis", "Patient Count");
                    results = reportsService.getPatientCountPerDiagnosis();
                    break;
                case "3":
                    // List all patients of personal doctor
                    if (doctorId == null) {
                        model.addAttribute("errorMessage", "Doctor is required for this query.");
                        break;
                    }
                    headers = List.of("Patient Name");
                    results = reportsService.getPatientsWithDoctor(doctorId);
                    break;
                case "4":
                    // Sum patients per personal doctor
                    headers = List.of("Doctor Name", "Patient Count");
                    results = reportsService.getPatientCountPerDoctor();
                    break;
                case "5":
                    // Sum visits per doctor
                    headers = List.of("Doctor Name", "Visit Count");
                    results = reportsService.getVisitCountPerDoctor();
                    break;
                case "6":
                    // List all visits of patient
                    if (patientId == null) {
                        model.addAttribute("errorMessage", "Patient is required for this query.");
                        break;
                    }
                    headers = List.of("Visit Slot", "Treatment", "Doctor Name");
                    results = reportsService.getVisitsOfPatient(patientId);
                    break;
                case "7":
                    // Visits of all doctors in period
                    if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
                        model.addAttribute("errorMessage", "Start Date and End Date are required for this query.");
                        break;
                    }
                    headers = List.of("Doctor Name", "Visit Slot", "Patient Name");
                    results = reportsService.getVisitsInPeriod(startDate, endDate);
                    break;
                case "8":
                    // Visits of doctor in period
                    if (doctorId == null) {
                        model.addAttribute("errorMessage", "Doctor is required for this query.");
                        break;
                    }
                    if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
                        model.addAttribute("errorMessage", "Start Date and End Date are required for this query.");
                        break;
                    }
                    headers = List.of("Visit Slot", "Patient Name");
                    results = reportsService.getVisitsOfDoctorInPeriod(doctorId, startDate, endDate);
                    break;
                case "9":
                    // Month with most sick leaves by year
                    if (year == null) {
                        model.addAttribute("errorMessage", "Year is required for this query.");
                        break;
                    }
                    headers = List.of("Month", "Sick Leave Count");
                    results = reportsService.getMostSickLeavesMonth(year);
                    break;
                case "10":
                    // Doctor with most sick leaves total
                    headers = List.of("Doctor Name", "Sick Leave Count");
                    results = reportsService.getDoctorWithMostSickLeaves();
                    break;
                default:
                    model.addAttribute("errorMessage", "Invalid query type.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while running the query: " + e.getMessage());
        }

        // Add query results or error message
        model.addAttribute("headers", headers);
        model.addAttribute("results", results);

        // Add dropdown data and selected values back to the model
        model.addAttribute("doctors", userService.getDoctors());
        model.addAttribute("patients", userService.getPatients());
        model.addAttribute("diagnoses", diagnosisService.getAllDiagnoses());
        model.addAttribute("selectedQuery", query);
        model.addAttribute("selectedDiagnosisId", diagnosisId);
        model.addAttribute("selectedDoctorId", doctorId);
        model.addAttribute("selectedPatientId", patientId);
        model.addAttribute("selectedStartDate", startDate);
        model.addAttribute("selectedEndDate", endDate);
        model.addAttribute("selectedYear", year);

        return "reports/reports";
    }


}
