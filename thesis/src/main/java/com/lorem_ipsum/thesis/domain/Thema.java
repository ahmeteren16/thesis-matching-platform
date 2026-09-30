package com.lorem_ipsum.thesis.domain;


import org.springframework.data.annotation.Version;

import java.util.ArrayList;
import java.util.List;

public class Thema {


    private  Integer Id;
    private  String titel;
    private String beschreibung;
    private Integer betreuerId;
    private  List<String> voraussetzungen;
    @Version
    private Long version;


    public Thema(Integer Id, String titel, String beschreibung,Integer betreuerId, List<String> voraussetzungen) {
        this.Id = Id;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.betreuerId = betreuerId;
        this.voraussetzungen = (voraussetzungen == null) ? List.of() : List.copyOf(voraussetzungen);
    }

    public static Thema createThema(String titel, String beschreibung, List<String> voraussetzungen) {
        return new Thema(null, titel, beschreibung,null, voraussetzungen);
    }


    public String getTitel() {
        return titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public List<String> getVoraussetzungen() {
        return voraussetzungen;
    }


    public Integer getBetreuerId() {
        return betreuerId;
    }

    public void setBetreuerId(Integer betreuerId) {
        this.betreuerId = betreuerId;
    }

    public Integer getId() {
        return Id;
    }
}
