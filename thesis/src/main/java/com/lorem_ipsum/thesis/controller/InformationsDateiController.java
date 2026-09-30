package com.lorem_ipsum.thesis.controller;

import com.lorem_ipsum.thesis.config.Betreuer;
import com.lorem_ipsum.thesis.domain.BetreuerProfile;
import com.lorem_ipsum.thesis.domain.InformationsDatei;
import com.lorem_ipsum.thesis.service.BetreuerService;
import com.lorem_ipsum.thesis.service.InformationsDateiService;
import com.lorem_ipsum.thesis.service.MarkdownService;
import com.lorem_ipsum.thesis.service.NichtVorhandenException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequestMapping("/betreuer/{betreuerId}/dateien")
public class InformationsDateiController {

    private final InformationsDateiService dateiService;
    private final BetreuerService betreuerService;
    private final MarkdownService markdownService;

    public InformationsDateiController(InformationsDateiService dateiService,
                                      BetreuerService betreuerService,
                                      MarkdownService markdownService) {
        this.dateiService = dateiService;
        this.betreuerService = betreuerService;
        this.markdownService = markdownService;
    }

    @Betreuer
    @GetMapping("/upload")
    public String showUploadForm(@PathVariable Integer betreuerId, Model model) {
        BetreuerProfile betreuer = betreuerService.findById(betreuerId);
        model.addAttribute("betreuer", betreuer);
        return "dateien/upload";
    }

    @Betreuer
    @PostMapping("/upload")
    public String uploadDatei(@PathVariable Integer betreuerId,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam("titel") String titel,
                              @RequestParam(value = "beschreibung", required = false) String beschreibung,
                              OAuth2AuthenticationToken authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            String uploaderName = authentication.getPrincipal().getAttribute("name");
            if (uploaderName == null) {
                uploaderName = authentication.getName();
            }

            dateiService.uploadDatei(betreuerId, uploaderName, titel, beschreibung, file);
            redirectAttributes.addFlashAttribute("message", "Datei erfolgreich hochgeladen!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/betreuer/details/{betreuerId}";
    }

    @GetMapping("/{dateiId}/download")
    public ResponseEntity<Resource> downloadDatei(@PathVariable Integer betreuerId,
                                                  @PathVariable Integer dateiId) {
        try {
            InformationsDatei datei = dateiService.findById(dateiId);
            
            if (!datei.getBetreuerId().equals(betreuerId)) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = dateiService.getFilePath(datei.getDateiName());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = datei.getDateiTyp().getMimeType();
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "attachment; filename=\"" + datei.getOriginalDateiName() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{dateiId}/view")
    public String viewMarkdown(@PathVariable Integer betreuerId,
                              @PathVariable Integer dateiId,
                              Model model) {
        InformationsDatei datei = dateiService.findById(dateiId);

        if (!datei.getBetreuerId().equals(betreuerId)) {
            throw new NichtVorhandenException();
        }

        if (datei.getDateiTyp() != InformationsDatei.DateiTyp.MARKDOWN) {
            throw new IllegalArgumentException("Nur Markdown-Dateien können angezeigt werden");
        }

        try {
            Path filePath = dateiService.getFilePath(datei.getDateiName());
            String markdownContent = Files.readString(filePath);
            String htmlContent = markdownService.convertToHtml(markdownContent);

            model.addAttribute("datei", datei);
            model.addAttribute("htmlContent", htmlContent);
            model.addAttribute("betreuer", betreuerService.findById(betreuerId));

            return "dateien/markdown-view";
        } catch (IOException e) {
            throw new RuntimeException("Markdown-Datei konnte nicht gelesen werden", e);
        }
    }

    @Betreuer
    @PostMapping("/{dateiId}/delete")
    public String deleteDatei(@PathVariable Integer betreuerId,
                             @PathVariable Integer dateiId,
                             RedirectAttributes redirectAttributes) {
        try {
            InformationsDatei datei = dateiService.findById(dateiId);
            
            if (!datei.getBetreuerId().equals(betreuerId)) {
                throw new NichtVorhandenException();
            }

            dateiService.deleteById(dateiId);
            redirectAttributes.addFlashAttribute("message", "Datei erfolgreich gelöscht!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/betreuer/details/" + betreuerId;
    }
}