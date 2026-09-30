CREATE TABLE informations_datei (
    id SERIAL PRIMARY KEY,
    betreuer_id INTEGER NOT NULL,
    uploader_name VARCHAR(255) NOT NULL,
    upload_datum TIMESTAMP NOT NULL,
    titel VARCHAR(255) NOT NULL,
    beschreibung TEXT,
    datei_name VARCHAR(255) NOT NULL UNIQUE,
    original_datei_name VARCHAR(255) NOT NULL,
    datei_typ VARCHAR(20) NOT NULL,
    CONSTRAINT fk_betreuer FOREIGN KEY (betreuer_id) REFERENCES betreuer_profile(id) ON DELETE CASCADE
);

CREATE INDEX idx_informations_datei_betreuer_id ON informations_datei(betreuer_id);