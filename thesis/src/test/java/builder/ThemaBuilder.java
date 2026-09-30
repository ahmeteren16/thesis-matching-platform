package builder;

import com.lorem_ipsum.thesis.domain.Thema;

import java.util.List;

public class ThemaBuilder {

    private String title = "Default Thema";
    private String beschreibung = null;
    private Integer betreuerId = null;
    private List<String> voraussetzungen = List.of("DefaultFach");


    public static ThemaBuilder aThema() {
        return new ThemaBuilder();
    }

    public ThemaBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ThemaBuilder withBetreuerId(Integer id) {
        this.betreuerId = id;
        return this;
    }

    public ThemaBuilder withBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
        return this;
    }

    public ThemaBuilder withVoraussetzungen(List<String> voraussetzungen) {
        this.voraussetzungen = (voraussetzungen == null) ? List.of() : List.copyOf(voraussetzungen);
        return this;
    }


    public Thema build() {
        Thema thema = Thema.createThema(title,beschreibung, voraussetzungen);
        if (betreuerId != null){
            thema.setBetreuerId(betreuerId);
        }
        return thema;
    }
}
