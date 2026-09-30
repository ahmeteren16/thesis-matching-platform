package com.lorem_ipsum.thesis.controller;


import com.lorem_ipsum.thesis.domain.BetreuerProfile;
import com.lorem_ipsum.thesis.domain.Thema;
import com.lorem_ipsum.thesis.service.BetreuerService;
import com.lorem_ipsum.thesis.service.ThemaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Controller
public class MatchingController {

    @Autowired
    private BetreuerService betreuerService;

    @Autowired
    private ThemaService themaService;

    @GetMapping("/matching")
    public String showMatching(
            Model model,
            @Valid @RequestParam(required = false) String voraussetzung,
            @Valid @RequestParam(required = false) String titel
    ) {

        if (voraussetzung == null & titel == null) {
            setEmptyModel(model);
            return "matching/index";
        }


        List<Thema> themen = (titel == null)
                ? themaService.findByVoraussetzungen(voraussetzung)
                : List.of(themaService.findByTitel(titel));


        List<BetreuerProfile> betreuer = findBetreuerFrom(themen);


        model.addAttribute("voraussetzung", voraussetzung);
        model.addAttribute("themen", themen);
        model.addAttribute("betreuer", betreuer);

        return "matching/index";
    }

    private void setEmptyModel(Model model) {
        model.addAttribute("themen", List.of());
        model.addAttribute("betreuer", List.of());
        model.addAttribute("voraussetzung", "");
    }

    private List<BetreuerProfile> findBetreuerFrom(List<Thema> themen) {
        List<Integer> betreuerIds = themen.stream()
                .map(Thema::getBetreuerId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        return betreuerIds.stream()
                .map(betreuerService::findById)
                .toList();
    }

    @PostMapping("/matching/assign")
    public String assignThemaToBetreuer(@RequestParam String titel1, @RequestParam Integer betreuerId) {
        themaService.assignToBetreuer(titel1, betreuerId);
        return "redirect:/matching";
    }

    @GetMapping("/themen")
    public String showThemen(Model model) {
        model.addAttribute("themen", themaService.findAll());
        return "matching/themen";
    }

    @PostMapping("/themen/add")
    public String addThemen(@Valid @ModelAttribute ThemaAddRequest request,
                            BindingResult bindingResult,
                            RedirectAttributes ra) {

        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("errorMessage","Geben Sie bitte gültige Parametern ein");
            return "redirect:/matching/addthema";
        }

        themaService.save(Thema.createThema(request.titel(), request.beschreibung(), request.voraussetzungen()));
        return "redirect:/themen";
    }

    @GetMapping("/themen/add")
    public String addThemen(Model model) {
        model.addAttribute("thema", new ThemaAddRequest("", "", List.of()));
        List<BetreuerProfile> betreuer = betreuerService.getAllBetreuer();
        List<Thema> themen = themaService.findAll();
        model.addAttribute("betreuer", betreuer);
        model.addAttribute("themen", themen);


        return "matching/addthema";
    }


}
