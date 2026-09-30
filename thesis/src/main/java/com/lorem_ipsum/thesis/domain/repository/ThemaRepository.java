package com.lorem_ipsum.thesis.domain.repository;


import java.util.Collection;
import java.util.Optional;

import com.lorem_ipsum.thesis.domain.Thema;


public interface ThemaRepository {
    Optional<Thema> findById(Integer id);

    Collection<Thema> findAll();

    Optional<Thema> findByTitel(String titel);

    Collection<Thema> findByVoraussetzung(String voraussetzung);

    void save(Thema thema);

}
