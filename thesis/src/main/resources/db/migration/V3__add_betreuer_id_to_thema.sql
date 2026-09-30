ALTER TABLE thema_dto
    ADD COLUMN betreuer_id INTEGER;

ALTER TABLE thema_dto
    ADD CONSTRAINT fk_thema_betreuer_profile
        FOREIGN KEY (betreuer_id)
            REFERENCES betreuer_profile(id)
            ON DELETE SET NULL;