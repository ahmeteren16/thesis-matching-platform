package com.lorem_ipsum.thesis.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.lorem_ipsum.thesis.domain.Thema;
import com.lorem_ipsum.thesis.domain.repository.ThemaRepository;

@Repository
public class ThemaRepositoryImpl implements ThemaRepository {

    private final ThemaDbRepo db;

    public ThemaRepositoryImpl(ThemaDbRepo db) {
        this.db = db;
    }


    @Override
    public Optional<Thema> findById(Integer id) {
        return db.findById(id).map(this::toThema);
    }

    @Override
    public List<Thema> findAll() {
        return db.findAll().stream()
                .map(this::toThema)
                .toList();
    }

    @Override
    public Optional<Thema> findByTitel(String titel) {
        return db.findThemaByTitel(titel).map(this::toThema);
    }

    @Override
    public List<Thema> findByVoraussetzung(String voraussetzung) {
        return db.findThemasByVoraussetzungenContainingIgnoreCase(voraussetzung)
                .stream()
                .map(this::toThema)
                .toList();
    }

    @Override
    public void save(Thema thema) {
        ThemaDto dto = toThemaDto(thema);

        db.save(dto);
    }

    private Thema toThema(ThemaDto dto) {
        return new Thema(dto.id(), dto.titel(), dto.beschreibung(), dto.betreuerId(), dto.voraussetzungen());
    }

    private ThemaDto toThemaDto(Thema thema) {
        return new ThemaDto(thema.getId(), thema.getTitel(), thema.getBeschreibung(), thema.getBetreuerId() ,thema.getVoraussetzungen());
    }
}
