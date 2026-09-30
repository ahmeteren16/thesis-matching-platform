package com.lorem_ipsum.thesis.persistence;

import com.lorem_ipsum.thesis.domain.InformationsDatei;
import com.lorem_ipsum.thesis.domain.repository.InformationsDateiRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InformationsDateiRepositoryImpl implements InformationsDateiRepository {

    private final InformationsDateiDbRepo db;

    public InformationsDateiRepositoryImpl(InformationsDateiDbRepo db) {
        this.db = db;
    }

    @Override
    public Optional<InformationsDatei> findById(Integer id) {
        return db.findById(id).map(this::toInformationsDatei);
    }

    @Override
    public List<InformationsDatei> findByBetreuerId(Integer betreuerId) {
        return db.findByBetreuerId(betreuerId).stream()
                .map(this::toInformationsDatei)
                .toList();
    }

    @Override
    public InformationsDatei save(InformationsDatei datei) {
        InformationsDateiDto dto = toDto(datei);
        InformationsDateiDto saved = db.save(dto);
        return toInformationsDatei(saved);
    }

    @Override
    public void deleteById(Integer id) {
        db.deleteById(id);
    }

    private InformationsDatei toInformationsDatei(InformationsDateiDto dto) {
        return new InformationsDatei(
                dto.id(),
                dto.betreuerId(),
                dto.uploaderName(),
                dto.uploadDatum(),
                dto.titel(),
                dto.beschreibung(),
                dto.dateiName(),
                dto.originalDateiName(),
                InformationsDatei.DateiTyp.valueOf(dto.dateiTyp())
        );
    }

    private InformationsDateiDto toDto(InformationsDatei datei) {
        return new InformationsDateiDto(
                datei.getId(),
                datei.getBetreuerId(),
                datei.getUploaderName(),
                datei.getUploadDatum(),
                datei.getTitel(),
                datei.getBeschreibung(),
                datei.getDateiName(),
                datei.getOriginalDateiName(),
                datei.getDateiTyp().name()
        );
    }
}