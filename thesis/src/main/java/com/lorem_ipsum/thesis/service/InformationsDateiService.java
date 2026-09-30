package com.lorem_ipsum.thesis.service;

import com.lorem_ipsum.thesis.domain.InformationsDatei;
import com.lorem_ipsum.thesis.domain.repository.InformationsDateiRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class InformationsDateiService {

    private final InformationsDateiRepository repository;
    private final Path fileStorageLocation;
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    public InformationsDateiService(InformationsDateiRepository repository,
                                   @Value("${file.upload-dir:uploads}") String uploadDir) {
        this.repository = repository;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Konnte Upload-Verzeichnis nicht erstellen", e);
        }
    }

    public InformationsDatei uploadDatei(Integer betreuerId, String uploaderName,
                                        String titel, String beschreibung,
                                        MultipartFile file) {
        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new UngueltigerDateiTypException("Dateiname ist ungültig");
        }

        InformationsDatei.DateiTyp dateiTyp = InformationsDatei.DateiTyp.fromExtension(originalFilename);
        String systemFilename = generateSystemFilename(dateiTyp);

        try {
            Path targetLocation = this.fileStorageLocation.resolve(systemFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            InformationsDatei datei = InformationsDatei.createDatei(
                    betreuerId, uploaderName, titel, beschreibung,
                    systemFilename, originalFilename, dateiTyp
            );

            return repository.save(datei);
        } catch (IOException e) {
            throw new RuntimeException("Datei konnte nicht gespeichert werden", e);
        }
    }

    public List<InformationsDatei> getDateienFuerBetreuer(Integer betreuerId) {
        return repository.findByBetreuerId(betreuerId);
    }

    public InformationsDatei findById(Integer id) {
        return repository.findById(id).orElseThrow(NichtVorhandenException::new);
    }

    public Path getFilePath(String filename) {
        return this.fileStorageLocation.resolve(filename).normalize();
    }

    public void deleteById(Integer id) {
        InformationsDatei datei = findById(id);
        try {
            Path filePath = getFilePath(datei.getDateiName());
            Files.deleteIfExists(filePath);
            repository.deleteById(id);
        } catch (IOException e) {
            throw new RuntimeException("Datei konnte nicht gelöscht werden", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new UngueltigerDateiTypException("Datei ist leer");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new DateiZuGrossException("Datei ist größer als 10 MB");
        }
    }

    private String generateSystemFilename(InformationsDatei.DateiTyp dateiTyp) {
        return UUID.randomUUID().toString() + dateiTyp.getExtension();
    }
}