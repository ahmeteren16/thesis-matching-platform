package com.lorem_ipsum.thesis.persistence;


import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ThemaDbRepo extends CrudRepository<ThemaDto, Integer> {

    Optional<ThemaDto> findThemaByTitel(String titel);

    @Query("""
              SELECT * FROM thema_dto tm
              WHERE EXISTS (
                SELECT 1
                FROM unnest(tm.voraussetzungen) v
                WHERE lower(v) = lower(:voraussetzungen)
              )
            """)
    List<ThemaDto> findThemasByVoraussetzungenContainingIgnoreCase(@Param("voraussetzungen") String voraussetzung);

    List<ThemaDto> findAll();
}

