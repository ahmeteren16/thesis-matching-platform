package com.lorem_ipsum.thesis.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table("thema_dto")
public record ThemaDto(@Id Integer id, String titel, String beschreibung, @Column("betreuer_id") Integer betreuerId, List<String> voraussetzungen) {
    public ThemaDto {
        voraussetzungen = (voraussetzungen == null) ? List.of() : List.copyOf(voraussetzungen);
    }
}
