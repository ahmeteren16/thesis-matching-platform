package com.lorem_ipsum.thesis.controller;

import com.lorem_ipsum.thesis.service.NichtVorhandenException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice(assignableTypes = BetreuerController.class)
public class BetreuerExceptionHandler {

    @ExceptionHandler(NichtVorhandenException.class)
    public String handleNichtVorhanden(
            NichtVorhandenException ex,
            Model model,
            jakarta.servlet.http.HttpServletResponse response) {

        response.setStatus(404);
        model.addAttribute("errorMessage", "Kein Betreuer gefunden");
        return "betreuer/liste";
    }

    @ExceptionHandler(org.springframework.dao.OptimisticLockingFailureException.class)
    public String handleOptimisticLock(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Änderungskonflikt");
        model.addAttribute("errorMessage",
                "Der Datensatz wurde während Ihrer Bearbeitung von einer anderen Person geändert. "
                        + "Bitte laden Sie die Seite neu und versuchen Sie es erneut.");
        return "error";
    }
}
