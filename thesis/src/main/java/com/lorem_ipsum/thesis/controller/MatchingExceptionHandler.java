package com.lorem_ipsum.thesis.controller;

import com.lorem_ipsum.thesis.service.NichtVorhandenException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = MatchingController.class)
public class MatchingExceptionHandler {

    @ExceptionHandler(NichtVorhandenException.class)
    public String handleNichtVorhanden(
            NichtVorhandenException ex,
            Model model,
            jakarta.servlet.http.HttpServletResponse response) {

        response.setStatus(404);
        model.addAttribute("errorMessage", "Kein Betreuer gefunden");
        return "matching/index";
    }
}
