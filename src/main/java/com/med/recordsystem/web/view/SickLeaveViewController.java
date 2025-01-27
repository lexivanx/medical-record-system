package com.med.recordsystem.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SickLeaveViewController {
    @GetMapping("/sick-leaves")
    public String viewSickLeavesPage() {
        return "sick-leaves";
    }
}
