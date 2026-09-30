package com.lorem_ipsum.thesis.service;

import com.lorem_ipsum.thesis.domain.Thema;
import com.lorem_ipsum.thesis.domain.repository.BetreuerRepository;
import com.lorem_ipsum.thesis.domain.repository.ThemaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ThemaService {

    private final ThemaRepository repository;
    private final BetreuerRepository betreuerRepository;

    public ThemaService(ThemaRepository repository, BetreuerRepository betreuerRepository) {
        this.repository = repository;
        this.betreuerRepository = betreuerRepository;
    }

    public Thema findByTitel(String titel) {
        return repository.findByTitel(titel).orElseThrow(NichtVorhandenException::new);

    }

    public List<Thema> findAll() {
        return repository.findAll().stream().toList();

    }

    public List<Thema> findByVoraussetzungen(String voraussetzung) {
        List<Thema> list = repository.findByVoraussetzung(voraussetzung).stream().toList();
        if (list.isEmpty()) {
            throw new NichtVorhandenException();
        } else {
            return list;
        }
    }

    @Transactional
    public void assignToBetreuer(String titel, Integer betreuerId) {
        if (betreuerRepository.findById(betreuerId).isEmpty()) {
            throw new NichtVorhandenException();
        }
        Thema thema = repository.findByTitel(titel)
                .orElseThrow(() -> new NichtVorhandenException());
        thema.setBetreuerId(betreuerId);
        repository.save(thema);
    }

    @Transactional
    public void save(Thema thema){
        repository.save(thema);
    }
}
