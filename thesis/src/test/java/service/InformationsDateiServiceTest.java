package service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

import com.lorem_ipsum.thesis.domain.InformationsDatei;
import com.lorem_ipsum.thesis.domain.repository.InformationsDateiRepository;
import com.lorem_ipsum.thesis.service.DateiZuGrossException;
import com.lorem_ipsum.thesis.service.InformationsDateiService;
import com.lorem_ipsum.thesis.service.NichtVorhandenException;
import com.lorem_ipsum.thesis.service.UngueltigerDateiTypException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("InformationsDateiService – Datei-Upload und Verwaltung")
class InformationsDateiServiceTest {

    @TempDir
    Path tempDir;

    @Mock
    InformationsDateiRepository repository;

    InformationsDateiService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new InformationsDateiService(repository, tempDir.toString());
    }

    @Test
    @DisplayName("Datei wird erfolgreich hochgeladen und in der Datenbank gespeichert")
    void test_01() throws Exception {
        byte[] content = "hello".getBytes();
        MultipartFile file = new MockMultipartFile(
                "file",
                "document.pdf",
                "application/pdf",
                content
        );

        when(repository.save(any(InformationsDatei.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        InformationsDatei saved = service.uploadDatei(
                1,
                "Ahmet",
                "Titel",
                "Beschreibung",
                file
        );

        assertThat(saved).isNotNull();
        verify(repository).save(any(InformationsDatei.class));

        Path written = service.getFilePath(saved.getDateiName());
        assertThat(Files.exists(written)).isTrue();
        assertThat(Files.readAllBytes(written)).isEqualTo(content);
    }

    @Test
    @DisplayName("Leere Datei führt zu UngueltigerDateiTypException")
    void test_02() {
        MultipartFile empty = new MockMultipartFile(
                "file", "a.pdf", "application/pdf", new byte[0]
        );

        assertThatThrownBy(() ->
                service.uploadDatei(1, "u", "t", "b", empty)
        ).isInstanceOf(UngueltigerDateiTypException.class);

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Datei größer als 10 MB führt zu DateiZuGrossException")
    void test_03() {
        byte[] big = new byte[(10 * 1024 * 1024) + 1];
        MultipartFile file = new MockMultipartFile(
                "file", "a.pdf", "application/pdf", big
        );

        assertThatThrownBy(() ->
                service.uploadDatei(1, "u", "t", "b", file)
        ).isInstanceOf(DateiZuGrossException.class);

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Ungültiger Dateiname führt zu UngueltigerDateiTypException")
    void test_04() {
        MultipartFile file = new MockMultipartFile(
                "file", "", "application/pdf", "x".getBytes()
        );

        assertThatThrownBy(() ->
                service.uploadDatei(1, "u", "t", "b", file)
        ).isInstanceOf(UngueltigerDateiTypException.class);

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Dateien eines Betreuers werden korrekt aus dem Repository geladen")
    void test_05() {
        when(repository.findByBetreuerId(5))
                .thenReturn(List.of(mock(InformationsDatei.class)));

        List<InformationsDatei> result = service.getDateienFuerBetreuer(5);

        assertThat(result).hasSize(1);
        verify(repository).findByBetreuerId(5);
    }

    @Test
    @DisplayName("findById wirft NichtVorhandenException, wenn Datei nicht existiert")
    void test_06() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99))
                .isInstanceOf(NichtVorhandenException.class);
    }

    @Test
    @DisplayName("Datei und Datenbankeintrag werden beim Löschen entfernt")
    void test_07() throws Exception {
        Path file = tempDir.resolve("abc.pdf");
        Files.write(file, "data".getBytes());

        InformationsDatei entity = mock(InformationsDatei.class);
        when(entity.getDateiName()).thenReturn("abc.pdf");
        when(repository.findById(10)).thenReturn(Optional.of(entity));

        service.deleteById(10);

        assertThat(Files.exists(file)).isFalse();
        verify(repository).deleteById(10);
    }

    @Test
    @DisplayName("Datenbankeintrag wird gelöscht, auch wenn Datei nicht existiert")
    void test_08() {
        InformationsDatei entity = mock(InformationsDatei.class);
        when(entity.getDateiName()).thenReturn("nicht-vorhanden.pdf");
        when(repository.findById(10)).thenReturn(Optional.of(entity));

        service.deleteById(10);

        verify(repository).deleteById(10);
    }
}
