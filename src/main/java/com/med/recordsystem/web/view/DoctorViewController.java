package com.med.recordsystem.web.view;

import com.med.recordsystem.exception.DataNotFoundException;
import com.med.recordsystem.model.SickLeave;
import com.med.recordsystem.model.UserDiagnosis;
import com.med.recordsystem.model.Visit;
import com.med.recordsystem.model.User;
import com.med.recordsystem.service.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctor")
public class DoctorViewController {

    private final VisitService visitService;
    private final UserDiagnosisService userDiagnosisService;
    private final SickLeaveService sickLeaveService;
    private final UserService userService;
    private final DiagnosisService diagnosisService;

    public DoctorViewController(VisitService visitService, UserDiagnosisService userDiagnosisService,
                                SickLeaveService sickLeaveService, UserService userService,
                                DiagnosisService diagnosisService) {
        this.visitService = visitService;
        this.userDiagnosisService = userDiagnosisService;
        this.sickLeaveService = sickLeaveService;
        this.userService = userService;
        this.diagnosisService = diagnosisService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/visits")
    public String viewVisits(Model model) {
        model.addAttribute("visits", visitService.getAllVisits());
        model.addAttribute("patients", userService.getPatients());
        return "doctor/visits";
    }

    @PostMapping("/visits")
    public String createVisit(@ModelAttribute Visit visit, @AuthenticationPrincipal UserDetails userDetails) {
        User doctor = userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        visit.setDoctor(doctor);
        visitService.saveVisit(visit);

        return "redirect:/doctor/visits";
    }


    @GetMapping("/visits/{id}/edit")
    public String editVisit(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Visit visit = visitService.getVisitForDoctor(id, userDetails.getUsername());
        model.addAttribute("visit", visit);
        model.addAttribute("patients", userService.getPatients());
        return "doctor/edit-visit";
    }


    @PostMapping("/visits/{id}/delete")
    public String deleteVisit(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        visitService.deleteVisitForDoctor(id, userDetails.getUsername());
        return "redirect:/doctor/visits";
    }

    @PostMapping("/visits/{id}/update")
    public String updateVisit(@PathVariable Long id, @ModelAttribute Visit visit, @AuthenticationPrincipal UserDetails userDetails) {
        visitService.updateVisitForDoctor(id, visit, userDetails.getUsername());
        return "redirect:/doctor/visits";
    }

    @GetMapping("/user-diagnoses")
    public String viewUserDiagnoses(Model model) {
        model.addAttribute("userDiagnoses", userDiagnosisService.getAllUserDiagnoses());
        model.addAttribute("patients", userService.getPatients());
        model.addAttribute("diagnoses", diagnosisService.getAllDiagnoses());
        return "doctor/user-diagnoses";
    }


    @PostMapping("/user-diagnoses")
    public String createUserDiagnosis(@RequestParam Long patientId, @RequestParam Long diagnosisId,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        try {
            userDiagnosisService.createUserDiagnosisForDoctor(patientId, diagnosisId, userDetails.getUsername());
        } catch (AccessDeniedException e) {
            return "redirect:/access-denied";
        } catch (DataNotFoundException e) {
            return "redirect:/error/data-not-found";
        }
        return "redirect:/doctor/user-diagnoses";
    }

    @PostMapping("/user-diagnoses/{id}/delete")
    public String deleteUserDiagnosis(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            userDiagnosisService.deleteUserDiagnosisForDoctor(id, userDetails.getUsername());
        } catch (IllegalArgumentException | DataNotFoundException e) {
            return "redirect:/error/data-not-found";
        } catch (AccessDeniedException e) {
            return "redirect:/access-denied";
        }
        return "redirect:/doctor/user-diagnoses";
    }

    @PostMapping("/user-diagnoses/{id}/update")
    public String updateUserDiagnosis(@PathVariable String id, @ModelAttribute UserDiagnosis diagnosis,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        userDiagnosisService.updateUserDiagnosisForDoctor(id, diagnosis, userDetails.getUsername());
        return "redirect:/doctor/user-diagnoses";
    }

    @GetMapping("/sick-leaves")
    public String viewSickLeaves(Model model) {
        model.addAttribute("sickLeaves", sickLeaveService.getAllSickLeaves());
        model.addAttribute("patients", userService.getPatients());
        return "doctor/sick-leaves";
    }

    @PostMapping("/sick-leaves")
    public String createSickLeave(@ModelAttribute SickLeave sickLeave, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            sickLeaveService.saveSickLeave(sickLeave, userDetails.getUsername());
        } catch (AccessDeniedException e) {
            return "redirect:/access-denied";
        } catch (DataNotFoundException e) {
            return "redirect:/error/data-not-found";
        }
        return "redirect:/doctor/sick-leaves";
    }

    @GetMapping("/sick-leaves/{id}/edit")
    public String editSickLeave(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        SickLeave sickLeave = sickLeaveService.getSickLeaveForDoctor(id, userDetails.getUsername());
        model.addAttribute("sickLeave", sickLeave);
        model.addAttribute("patients", userService.getPatients());
        return "doctor/edit-sick-leave";
    }

    @PostMapping("/sick-leaves/{id}/delete")
    public String deleteSickLeave(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            sickLeaveService.deleteSickLeaveForDoctor(id, userDetails.getUsername());
        } catch (AccessDeniedException e) {
            return "redirect:/access-denied";
        } catch (DataNotFoundException e) {
            return "redirect:/error/data-not-found";
        }
        return "redirect:/doctor/sick-leaves";
    }

    @PostMapping("/sick-leaves/{id}/update")
    public String updateSickLeave(@PathVariable Long id, @ModelAttribute SickLeave sickLeave,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        try {
            sickLeaveService.updateSickLeaveForDoctor(id, sickLeave, userDetails.getUsername());
        } catch (AccessDeniedException e) {
            return "redirect:/access-denied";
        } catch (DataNotFoundException e) {
            return "redirect:/error/data-not-found";
        }
        return "redirect:/doctor/sick-leaves";
    }

}

