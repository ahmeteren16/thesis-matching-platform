package service;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import builder.BetreuerProfileBuilder;
import builder.ThemaBuilder;
import com.lorem_ipsum.thesis.domain.BetreuerProfile;
import com.lorem_ipsum.thesis.domain.repository.BetreuerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lorem_ipsum.thesis.domain.Thema;
import com.lorem_ipsum.thesis.domain.repository.ThemaRepository;
import com.lorem_ipsum.thesis.service.NichtVorhandenException;
import com.lorem_ipsum.thesis.service.ThemaService;

public class ThemaServiceTest {

    ThemaRepository themaRepository = mock(ThemaRepository.class);
    ThemaService themaService;
    BetreuerRepository betreuerRepository = mock(BetreuerRepository.class);

    @BeforeEach
    void setUp() {
        themaService = new ThemaService(themaRepository, betreuerRepository);

    }

    @Test
    @DisplayName("Ein Thema kann hinzugefügt werden")
    void test_1() {
        Thema thema1 = ThemaBuilder.aThema()
                .withTitle("Thema1")
                .build();
        ArgumentCaptor<Thema> captor = ArgumentCaptor.forClass(Thema.class);
        themaService.save(thema1);

        verify(themaRepository).save(captor.capture());
        Thema saved = captor.getValue();

        assertThat(saved.getTitel()).isEqualTo("Thema1");
    }

    @Test
    @DisplayName("Ein Thema kann mit dem Titel gefunden werden")
    void test_2() {

        Thema thema1 = ThemaBuilder.aThema()
                .withTitle("Thema1")
                .build();

        when(themaRepository.findByTitel("Thema1")).thenReturn(Optional.of(thema1));

        assertThat(themaService.findByTitel("Thema1").getTitel()).isEqualTo("Thema1");


    }

    @Test
    @DisplayName("Wenn ein Thema nicht existiert, wird NichtVorhandenException geworfen")
    void test_3() {
        when(themaRepository.findByTitel(any())).thenReturn(Optional.empty());
        when(themaRepository.findByVoraussetzung(any())).thenReturn(List.of());
        when(themaRepository.findAll()).thenReturn(List.of());

        assertThrows(NichtVorhandenException.class, () -> themaService.findByTitel(any()));
        assertThrows(NichtVorhandenException.class, () -> themaService.findByVoraussetzungen(any()));

    }

    @Test
    @DisplayName("Ein Thema kann einem Betreuer zugewiesen werden")
    void test_4() {

        Thema thema1 = ThemaBuilder.aThema()
                .withTitle("Thema1")
                .build();

        BetreuerProfile betreuer = BetreuerProfileBuilder.aBetreuer()
                .withGithubId("github1")
                .withName("Max Mustermann")
                .withEmail("max@example")
                .withTags(List.of("Java", "Spring"))
                .buildwithId(1);


        when(themaRepository.findByTitel("Thema1")).thenReturn(Optional.of(thema1));
        when(betreuerRepository.findById(1)).thenReturn(Optional.of(betreuer));
        themaService.assignToBetreuer(thema1.getTitel(),betreuer.getId());

        Thema geladen = themaRepository.findByTitel("Thema1").orElseThrow();


        assertThat(geladen.getBetreuerId()).isEqualTo(betreuer.getId());


    }

    @Test
    @DisplayName("Ein Thema kann einem Betreuer zugewiesen werden")
    void test_5() {

        Thema thema1 = ThemaBuilder.aThema()
                .withTitle("Thema1")
                .build();

        BetreuerProfile betreuer = BetreuerProfileBuilder.aBetreuer()
                .withGithubId("github1")
                .withName("Max Mustermann")
                .withEmail("max@example")
                .withTags(List.of("Java", "Spring"))
                .buildwithId(1);


        when(themaRepository.findByTitel("Thema1")).thenReturn(Optional.of(thema1));
        when(betreuerRepository.findById(1)).thenReturn(Optional.empty());


        assertThrows(NichtVorhandenException.class, () -> themaService.assignToBetreuer(thema1.getTitel(),betreuer.getId()));


    }

    @Test
    @DisplayName("Ein Thema kann mit einer Voraussetzung gefunden werden")
    void test_6() {

        Thema thema1 = ThemaBuilder.aThema()
                .withTitle("Thema1")
                .withVoraussetzungen(List.of("Java","Spring"))
                .build();

        when(themaRepository.findByVoraussetzung("Java")).thenReturn(List.of(thema1));

        assertThat(themaService.findByVoraussetzungen("Java")).contains(thema1);


    }

    @Test
    @DisplayName("findAll() gibt alle Themen zurück")
    void test_7() {

        Thema thema1 = ThemaBuilder.aThema()
                .withTitle("Thema1")
                .withVoraussetzungen(List.of("Java","Spring"))
                .build();

        Thema thema2 = ThemaBuilder.aThema()
                .withTitle("Thema2")
                .withVoraussetzungen(List.of("Java","Spring"))
                .build();

        when(themaRepository.findAll()).thenReturn(List.of(thema1,thema2));

        assertThat(themaService.findAll()).contains(thema1,thema2);


    }


}
