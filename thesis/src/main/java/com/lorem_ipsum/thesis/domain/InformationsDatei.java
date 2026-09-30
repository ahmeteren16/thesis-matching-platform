package com.lorem_ipsum.thesis.domain;

import com.lorem_ipsum.thesis.service.UngueltigerDateiTypException;
import java.time.LocalDateTime;

public class InformationsDatei {

    private Integer id;
    private Integer betreuerId;
    private String uploaderName;
    private LocalDateTime uploadDatum;
    private String titel;
    private String beschreibung;
    private String dateiName; // System-generierter Dateiname
    private String originalDateiName; // Original-Dateiname vom Upload
    private DateiTyp dateiTyp;

    public InformationsDatei(Integer id, Integer betreuerId, String uploaderName, 
                            LocalDateTime uploadDatum, String titel, String beschreibung,
                            String dateiName, String originalDateiName, DateiTyp dateiTyp) {
        this.id = id;
        this.betreuerId = betreuerId;
        this.uploaderName = uploaderName;
        this.uploadDatum = uploadDatum;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.dateiName = dateiName;
        this.originalDateiName = originalDateiName;
        this.dateiTyp = dateiTyp;
    }

    public static InformationsDatei createDatei(Integer betreuerId, String uploaderName,
                                               String titel, String beschreibung,
                                               String dateiName, String originalDateiName,
                                               DateiTyp dateiTyp) {
        return new InformationsDatei(null, betreuerId, uploaderName, LocalDateTime.now(),
                titel, beschreibung, dateiName, originalDateiName, dateiTyp);
    }

    public Integer getId() {
        return id;
    }

    public Integer getBetreuerId() {
        return betreuerId;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public LocalDateTime getUploadDatum() {
        return uploadDatum;
    }

    public String getTitel() {
        return titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public String getDateiName() {
        return dateiName;
    }

    public String getOriginalDateiName() {
        return originalDateiName;
    }

    public DateiTyp getDateiTyp() {
        return dateiTyp;
    }

    public enum DateiTyp {
        PDF("application/pdf", ".pdf"),
        ZIP("application/zip", ".zip"),
        MARKDOWN("text/markdown", ".md");

        private final String mimeType;
        private final String extension;

        DateiTyp(String mimeType, String extension) {
            this.mimeType = mimeType;
            this.extension = extension;
        }

        public String getMimeType() {
            return mimeType;
        }

        public String getExtension() {
            return extension;
        }

        public static DateiTyp fromExtension(String filename) {
            String lower = filename.toLowerCase();
            if (lower.endsWith(".pdf")) return PDF;
            if (lower.endsWith(".zip")) return ZIP;
            if (lower.endsWith(".md")) return MARKDOWN;
            throw new UngueltigerDateiTypException("Nur PDF, ZIP und Markdown-Dateien sind erlaubt");
        }
    }
}