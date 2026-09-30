package com.lorem_ipsum.thesis.controller;

import com.lorem_ipsum.thesis.config.AdminOnly;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThesisController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/home")
    public String homepage() {
        return "home";
    }

    @GetMapping("/adminseite")
    @AdminOnly
    public String adminseite(){
        return "admin/adminseite";
    }
}
