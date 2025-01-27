package com.med.recordsystem.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeViewController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
