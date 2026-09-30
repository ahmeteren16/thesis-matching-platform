package com.lorem_ipsum.thesis.persistence;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("informations_datei")
public record InformationsDateiDto(
        @Id Integer id,
        @Column("betreuer_id") Integer betreuerId,
        @Column("uploader_name") String uploaderName,
        @Column("upload_datum") LocalDateTime uploadDatum,
        String titel,
        String beschreibung,
        @Column("datei_name") String dateiName,
        @Column("original_datei_name") String originalDateiName,
        @Column("datei_typ") String dateiTyp
) {
}