import builder.BetreuerProfileBuilder;
import builder.ThemaBuilder;
import com.lorem_ipsum.thesis.ThesisApplication;
import com.lorem_ipsum.thesis.domain.BetreuerProfile;
import com.lorem_ipsum.thesis.domain.Thema;
import com.lorem_ipsum.thesis.domain.repository.BetreuerRepository;
import com.lorem_ipsum.thesis.domain.repository.ThemaRepository;
import com.lorem_ipsum.thesis.persistence.BetreuerDbRepo;
import com.lorem_ipsum.thesis.persistence.BetreuerRepositoryImpl;
import com.lorem_ipsum.thesis.persistence.ThemaDbRepo;
import com.lorem_ipsum.thesis.persistence.ThemaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJdbcTest
@ContextConfiguration(classes = ThesisApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DatabaseTest {

    @Autowired
    BetreuerDbRepo Bdp;
    @Autowired
    ThemaDbRepo Tdp;

    BetreuerRepository betreueRrepository;
    ThemaRepository themaRepository;

    @BeforeEach
    void setUp() {
        betreueRrepository = new BetreuerRepositoryImpl(Bdp);
        themaRepository = new ThemaRepositoryImpl(Tdp);
    }

    @Test
    @DisplayName("Ein Betreuer kann gespeichert und aus der Datenbank geladen werden.")
    void test_1() {

        BetreuerProfile profile = BetreuerProfileBuilder.aBetreuer().buildwithoutId();
        betreueRrepository.save(profile);
        var geladen = betreueRrepository.findByGithubID(profile.getGithubID());
        assertThat(geladen).isPresent();

        assertThat(geladen.map(BetreuerProfile::getName).orElseThrow()).isEqualTo("Ahmet Eren");
        assertThat(geladen.map(BetreuerProfile::getGithubID).orElseThrow()).isEqualTo("ahmeteren22");
        assertThat(geladen.map(BetreuerProfile::getEmail).orElseThrow()).isEqualTo("aaa@aaa");
        assertThat(geladen.map(BetreuerProfile::getTags).orElseThrow()).hasSize(1);
        assertThat(geladen.orElseThrow().getId()).isNotNull();


    }

    @Test
    @DisplayName("Die Betreuer können über einen Tag gefunden werden.")
    void test_2() {
        BetreuerProfile profile = BetreuerProfileBuilder.aBetreuer().buildwithoutId();
        betreueRrepository.save(profile);
        List<BetreuerProfile> geladen = betreueRrepository.findByTag("Tag").stream().toList();
        assertThat(geladen)
                .extracting(BetreuerProfile::getGithubID)
                .contains("ahmeteren22");


    }

    @Test
    @DisplayName("Die Betreuer können über einen Email gefunden werden.")
    void test_02() {
        var profile = BetreuerProfileBuilder.aBetreuer()
                .withEmail("aaa@aaa")
                .withGithubId("ahmeteren22")
                .buildwithoutId();

        betreueRrepository.save(profile);

        var loaded = betreueRrepository.findByEmail("aaa@aaa").stream().toList();
        assertThat(loaded).extracting(BetreuerProfile::getGithubID).contains("ahmeteren22");

    }

    @Test
    @DisplayName("Nur Betreuer mit dem gesuchten Tag werden gefunden.")
    void test_3() {
        BetreuerProfile betreuer1 = BetreuerProfileBuilder.aBetreuer()
                .withGithubId("github1")
                .withName("Max Mustermann")
                .withEmail("max@example")
                .withTags(List.of("Java", "Spring"))
                .buildwithoutId();

        BetreuerProfile betreuer2 = BetreuerProfileBuilder.aBetreuer()
                .withGithubId("github2")
                .withName("Anna Schmidt")
                .withEmail("anna@example.com")
                .withTags(List.of("Python"))
                .buildwithoutId();

        betreueRrepository.save(betreuer1);
        betreueRrepository.save(betreuer2);


        List<BetreuerProfile> geladen = betreueRrepository.findByTag("Java").stream().toList();


        assertThat(geladen)
                .extracting(BetreuerProfile::getGithubID)
                .contains("github1")
                .doesNotContain("github2");
    }

    @Test
    @DisplayName("Ein Thema kann gespeichert und aus der Datenbank geladen werden.")
    void test_4() {
        Thema thema = ThemaBuilder.aThema()
                .withTitle("Thema")
                .withVoraussetzungen(List.of("Fach"))
                .build();

        themaRepository.save(thema);
        var geladen = themaRepository.findByTitel("Thema");

        assertThat(geladen).isPresent();

        assertThat(geladen.map(Thema::getTitel).orElseThrow()).isEqualTo("Thema");
        assertThat(geladen.map(Thema::getVoraussetzungen).orElseThrow()).contains("Fach");

    }

    @Test
    @DisplayName("Nur Themen mit der gesuchten Voraussetzung werden gefunden.")
    void test_5() {
        Thema thema1 = ThemaBuilder.aThema()
                .withTitle("Thema1")
                .withVoraussetzungen(List.of("Fach1"))
                .build();

        Thema thema2 = ThemaBuilder.aThema()
                .withTitle("Thema2")
                .withVoraussetzungen(List.of("Fach2"))
                .build();


        themaRepository.save(thema1);
        themaRepository.save(thema2);
        var geladen = themaRepository.findByVoraussetzung("Fach1");

        assertThat(geladen)
                .extracting(Thema::getTitel)
                .contains("Thema1")
                .doesNotContain("Thema2");


    }

    @Test
    @DisplayName("Ein Thema kann einem Betreuer zugewiesen werden")
    void test_6() {
        Thema thema1 = ThemaBuilder.aThema()
                .withTitle("Thema1")
                .withVoraussetzungen(List.of("Fach1"))
                .build();

        BetreuerProfile profile = BetreuerProfileBuilder.aBetreuer().buildwithoutId();

        themaRepository.save(thema1);
        betreueRrepository.save(profile);
        thema1 = themaRepository.findByTitel("Thema1").orElseThrow();
        profile = betreueRrepository.findByGithubID("ahmeteren22").orElseThrow();
        assertThat(thema1.getBetreuerId()).isNull();

        thema1.setBetreuerId(profile.getId());
        themaRepository.save(thema1);

        var geladen = themaRepository.findByTitel("Thema1");

        assertThat(geladen.orElseThrow().getBetreuerId()).isEqualTo(profile.getId());


    }

}
