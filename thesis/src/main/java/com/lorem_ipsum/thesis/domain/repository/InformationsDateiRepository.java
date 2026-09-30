package com.lorem_ipsum.thesis.domain.repository;

import com.lorem_ipsum.thesis.domain.InformationsDatei;

import java.util.List;
import java.util.Optional;

public interface InformationsDateiRepository {
    
    Optional<InformationsDatei> findById(Integer id);
    
    List<InformationsDatei> findByBetreuerId(Integer betreuerId);
    
    InformationsDatei save(InformationsDatei datei);
    
    void deleteById(Integer id);
}