package com.lorem_ipsum.thesis.controller;


import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lorem_ipsum.thesis.config.AdminOnly;
import com.lorem_ipsum.thesis.domain.BetreuerProfile;
import com.lorem_ipsum.thesis.domain.InformationsDatei;
import com.lorem_ipsum.thesis.service.BetreuerService;
import com.lorem_ipsum.thesis.service.InformationsDateiService;


@Controller
public class BetreuerController {

    @Autowired
    private BetreuerService betreuerService;

    @Autowired
    private InformationsDateiService dateiService;

    @GetMapping("/betreuer")
    public String listBetreuer(Model model) {
        List<BetreuerProfile> betreuer = betreuerService.getAllBetreuer();
        model.addAttribute("betreuer", betreuer);
        return "betreuer/liste";
    }

    @GetMapping("/betreuer/details/{id}")
    public String showBetreuer(@PathVariable Integer id, Model model) {
        BetreuerProfile betreuer = betreuerService.findById(id);
        List<InformationsDatei> dateien = dateiService.getDateienFuerBetreuer(id);
        
        model.addAttribute("betreuer", betreuer);
        model.addAttribute("dateien", dateien);
        return "betreuer/details";
    }

    @PostMapping("/betreuer/add")
    public String addBetreuer( @Valid @ModelAttribute BetreuerAddRequest request,
                               BindingResult bindingResult,
                               RedirectAttributes ra){
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("errorMessage","Geben Sie bitte gültige Parametern ein");
            return "redirect:/betreuer/add";
        }

        try{
            betreuerService.findByEmail(request.email());
            ra.addFlashAttribute("errorMessage","Mit dieser E-Mail-Adresse ist bereits ein Betreuer registriert");
            return "redirect:/betreuer/add";

        } catch (Exception e) {
            betreuerService.save(BetreuerProfile.createBetreuer(
                    request.githubID(), request.name(), request.email(), request.tags()
            ));
            return "redirect:/betreuer";
        }


    }

    @AdminOnly
    @GetMapping("/betreuer/add")
    public String addBetreuer(Model model){
        model.addAttribute("betreuer", new BetreuerAddRequest("", "", "", List.of()));
        return "betreuer/addbetreuer";
    }

}
