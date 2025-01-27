package com.med.recordsystem.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patients")
public class PatientViewController {

    @GetMapping("/visits")
    public String viewVisitsPage() {
        return "patient-visits";
    }
}

