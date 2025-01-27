package com.med.recordsystem.web.view;

import com.med.recordsystem.exception.DataNotFoundException;
import com.med.recordsystem.model.*;
import com.med.recordsystem.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private final UserService userService;
    private final VisitService visitService;
    private final SickLeaveService sickLeaveService;
    private final DiagnosisService diagnosisService;
    private final UserDiagnosisService userDiagnosisService;
    private final PersonalDocsService personalDocsService;

    public AdminViewController(UserService userService, VisitService visitService,
                               SickLeaveService sickLeaveService, DiagnosisService diagnosisService,
                               UserDiagnosisService userDiagnosisService,
                               PersonalDocsService personalDocsService) {
        this.userService = userService;
        this.visitService = visitService;
        this.sickLeaveService = sickLeaveService;
        this.diagnosisService = diagnosisService;
        this.userDiagnosisService = userDiagnosisService;
        this.personalDocsService = personalDocsService;
    }

    @GetMapping("/users")
    public String viewUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute User user, Model model) {
        if ("PATIENT".equals(user.getRole())) {
            if (user.getEgn() == null || String.valueOf(user.getEgn()).length() != 10) {
                model.addAttribute("errorMessage", "EGN must be exactly 10 digits.");
                model.addAttribute("users", userService.getAllUsers());
                return "admin/users";
            }
        }

        if ("DOCTOR".equals(user.getRole())) {
            if (user.getSpecialty() == null || user.getSpecialty().isEmpty()) {
                model.addAttribute("errorMessage", "Doctors require a specialty.");
                model.addAttribute("users", userService.getAllUsers());
                return "admin/users";
            }
        }

        if ("ADMIN".equals(user.getRole()) || "DOCTOR".equals(user.getRole())) {
            user.setIsEnsured(null);
        }

        userService.saveUserAsAdmin(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    @PostMapping("/users/{id}/update")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user, Model model) {
        if ("PATIENT".equals(user.getRole())) {
            if (user.getEgn() == null || String.valueOf(user.getEgn()).length() != 10) {
                model.addAttribute("errorMessage", "EGN must be exactly 10 digits.");
                model.addAttribute("user", userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found")));
                return "admin/edit-user";
            }
        }

        userService.updateUserAsAdmin(id, user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserAsAdmin(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/visits")
    public String viewVisits(Model model) {
        model.addAttribute("visits", visitService.getAllVisits());
        model.addAttribute("patients", userService.getPatients());
        model.addAttribute("doctors", userService.getDoctors());
        return "admin/visits";
    }

    @PostMapping("/visits")
    public String createVisit(@ModelAttribute Visit visit) {
        visitService.saveVisitAsAdmin(visit);
        return "redirect:/admin/visits";
    }

    @GetMapping("/visits/{id}/edit")
    public String editVisit(@PathVariable Long id, Model model) {
        Visit visit = visitService.getVisitById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found"));
        model.addAttribute("visit", visit);
        model.addAttribute("patients", userService.getPatients());
        model.addAttribute("doctors", userService.getDoctors());
        return "admin/edit-visit";
    }

    @PostMapping("/visits/{id}/update")
    public String updateVisit(@PathVariable Long id, @ModelAttribute Visit visit) {
        visitService.updateVisitAsAdmin(id, visit);
        return "redirect:/admin/visits";
    }

    @PostMapping("/visits/{id}/delete")
    public String deleteVisit(@PathVariable Long id) {
        visitService.deleteVisit(id);
        return "redirect:/admin/visits";
    }

    @GetMapping("/sick-leaves")
    public String viewSickLeaves(Model model) {
        model.addAttribute("sickLeaves", sickLeaveService.getAllSickLeaves());
        model.addAttribute("patients", userService.getPatients());
        model.addAttribute("doctors", userService.getDoctors());
        return "admin/sick-leaves";
    }

    @PostMapping("/sick-leaves")
    public String createSickLeave(@ModelAttribute SickLeave sickLeave) {
        sickLeaveService.saveSickLeave(sickLeave);
        return "redirect:/admin/sick-leaves";
    }

    @GetMapping("/sick-leaves/{id}/edit")
    public String editSickLeave(@PathVariable Long id, Model model) {
        SickLeave sickLeave = sickLeaveService.getSickLeaveById(id)
                .orElseThrow(() -> new DataNotFoundException("Sick leave not found"));
        model.addAttribute("sickLeave", sickLeave);
        model.addAttribute("patients", userService.getPatients());
        model.addAttribute("doctors", userService.getDoctors());
        return "admin/edit-sick-leave";
    }
    @PostMapping("/sick-leaves/{id}/update")
    public String updateSickLeave(@PathVariable Long id, @ModelAttribute SickLeave sickLeave) {
        sickLeaveService.updateSickLeaveForAdmin(id, sickLeave);
        return "redirect:/admin/sick-leaves";
    }

    @PostMapping("/sick-leaves/{id}/delete")
    public String deleteSickLeave(@PathVariable Long id) {
        sickLeaveService.deleteSickLeave(id);
        return "redirect:/admin/sick-leaves";
    }

    @GetMapping("/user-diagnoses")
    public String viewUserDiagnoses(Model model) {
        model.addAttribute("userDiagnoses", userDiagnosisService.getAllUserDiagnoses());
        model.addAttribute("users", userService.getPatients());
        model.addAttribute("diagnoses", diagnosisService.getAllDiagnoses());
        return "admin/user-diagnoses";
    }

    @PostMapping("/user-diagnoses")
    public String createUserDiagnosis(@RequestParam Long userId, @RequestParam Long diagnosisId) {
        userDiagnosisService.createUserDiagnosis(userId, diagnosisId);
        return "redirect:/admin/user-diagnoses";
    }

    @PostMapping("/user-diagnoses/{id}/delete")
    public String deleteUserDiagnosis(@PathVariable String id) {
        userDiagnosisService.deleteUserDiagnosisById(id);
        return "redirect:/admin/user-diagnoses";
    }


    @GetMapping("/personal-docs")
    public String viewPersonalDocs(Model model) {
        model.addAttribute("personalDocs", personalDocsService.getAllPersonalDocs());
        model.addAttribute("doctors", userService.getDoctors());
        model.addAttribute("patients", userService.getPatients());
        return "admin/personal-docs";
    }

    @PostMapping("/personal-docs")
    public String createPersonalDoc(@RequestParam Long doctorId, @RequestParam Long patientId, Model model) {
        try {
            personalDocsService.createPersonalDoc(doctorId, patientId);
        } catch (IllegalArgumentException e) {
            model.addAttribute("personalDocs", personalDocsService.getAllPersonalDocs());
            model.addAttribute("doctors", userService.getDoctors());
            model.addAttribute("patients", userService.getPatients());
            return "admin/personal-docs";
        }
        return "redirect:/admin/personal-docs";
    }

    @PostMapping("/personal-docs/{id}/delete")
    public String deletePersonalDoc(@PathVariable String id) {
        personalDocsService.deletePersonalDocById(id);
        return "redirect:/admin/personal-docs";
    }


    @GetMapping("/diagnoses")
    public String viewDiagnoses(Model model) {
        model.addAttribute("diagnoses", diagnosisService.getAllDiagnoses());
        return "admin/diagnoses";
    }

    @PostMapping("/diagnoses")
    public String createDiagnosis(@RequestParam String name, Model model) {
        try {
            diagnosisService.createDiagnosis(name);
        } catch (IllegalArgumentException e) {
            model.addAttribute("diagnoses", diagnosisService.getAllDiagnoses());
            return "admin/diagnoses";
        }
        return "redirect:/admin/diagnoses";
    }

    @PostMapping("/diagnoses/{id}/delete")
    public String deleteDiagnosis(@PathVariable Long id) {
        try {
            diagnosisService.deleteDiagnosis(id);
        } catch (DataNotFoundException e) {
            return "redirect:/error/data-not-found";
        }
        return "redirect:/admin/diagnoses";
    }

}
