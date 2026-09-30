package service;


import builder.BetreuerProfileBuilder;
import com.lorem_ipsum.thesis.domain.BetreuerProfile;
import com.lorem_ipsum.thesis.domain.repository.BetreuerRepository;
import com.lorem_ipsum.thesis.service.BetreuerService;
import com.lorem_ipsum.thesis.service.NichtVorhandenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class BetreuerServiceTest {

    BetreuerRepository repo = mock(BetreuerRepository.class);

    BetreuerService service;



    @BeforeEach
    void setUp() {
        service = new BetreuerService(repo);

    }

    @Test
    @DisplayName("Ein Betreuer kann hinzugefügt werden")
    void test_1() {
        BetreuerProfile profile = BetreuerProfileBuilder.aBetreuer().buildwithoutId();
        ArgumentCaptor<BetreuerProfile> captor = ArgumentCaptor.forClass(BetreuerProfile.class);
        service.save(profile);

        verify(repo).save(captor.capture());
        BetreuerProfile saved = captor.getValue();

        assertThat(saved.getGithubID()).isEqualTo("ahmeteren16");

    }

    @Test
    @DisplayName("Ein Betreuer kann mit Github Id,Name und Tag gefunden werden")
    void test_2() {
        BetreuerProfile profile = BetreuerProfileBuilder.aBetreuer().buildwithoutId();

        when(repo.findByGithubID("ahmeteren16")).thenReturn(Optional.of(profile));
        when(repo.findByName("Ahmet Eren")).thenReturn(Optional.of(profile));
        when(repo.findByEmail("aaa@aaa")).thenReturn(Optional.of(profile));
        when(repo.findByTag("tag")).thenReturn(List.of(profile));
        when(repo.findAll()).thenReturn(List.of(profile));


        BetreuerProfile profile1 = service.findByEmail("aaa@aaa");
        BetreuerProfile profile2 = service.findByName("Ahmet Eren");
        BetreuerProfile profile3 = service.findByGithubId("ahmeteren16");
        List<BetreuerProfile> profileList = service.findByTag("tag");
        List<BetreuerProfile> profileList2 = service.getAllBetreuer();


        assertThat(profile1.getEmail()).isEqualTo("aaa@aaa");
        assertThat(profile2.getName()).isEqualTo("Ahmet Eren");
        assertThat(profile3.getGithubID()).isEqualTo("ahmeteren16");
        assertThat(profileList).hasSize(1).contains(profile);
        assertThat(profileList2).hasSize(1).contains(profile);


    }

    @Test
    @DisplayName("Wenn ein Betreuer nicht existiert, wird NichtVorhandenException geworfen")
    void test_3() {

        when(repo.findByGithubID("ahmeteren16")).thenReturn(Optional.empty());
        when(repo.findByName("Ahmet Eren")).thenReturn(Optional.empty());
        when(repo.findByEmail("aaa@aaa")).thenReturn(Optional.empty());
        when(repo.findAll()).thenReturn(List.of());

        assertThrows(NichtVorhandenException.class, () -> service.findByGithubId(any()));
        assertThrows(NichtVorhandenException.class, () -> service.findByEmail(any()));
        assertThrows(NichtVorhandenException.class, () -> service.findByName(any()));


    }


}
