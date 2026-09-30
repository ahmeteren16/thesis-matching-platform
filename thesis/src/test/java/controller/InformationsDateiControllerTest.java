package controller;

import com.lorem_ipsum.thesis.ThesisApplication;
import com.lorem_ipsum.thesis.config.SecurityConfig;
import com.lorem_ipsum.thesis.controller.InformationsDateiController;
import com.lorem_ipsum.thesis.domain.BetreuerProfile;
import com.lorem_ipsum.thesis.domain.InformationsDatei;
import com.lorem_ipsum.thesis.service.BetreuerService;
import com.lorem_ipsum.thesis.service.InformationsDateiService;
import com.lorem_ipsum.thesis.service.MarkdownService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InformationsDateiController.class)
@WithMockUser
@ContextConfiguration(classes = ThesisApplication.class)
@Import(SecurityConfig.class)
class InformationsDateiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InformationsDateiService dateiService;

    @MockitoBean
    private BetreuerService betreuerService;

    @MockitoBean
    private MarkdownService markdownService;

    @Test
    @DisplayName("Datei-Upload sollte funktionieren")
    void test_uploadDatei() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", 
                "application/pdf", "Test".getBytes());

        mockMvc.perform(multipart("/betreuer/1/dateien/upload")
                        .file(file)
                        .with(csrf())
                        .with(oauth2Login().attributes(attrs -> attrs.put("name", "Test User")))
                        .param("titel", "Test Datei"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/betreuer/1"));

        verify(dateiService).uploadDatei(eq(1), anyString(), eq("Test Datei"), any(), any());
    }

    @Test
    @DisplayName("Datei-Download sollte funktionieren")
    void test_downloadDatei() throws Exception {
        InformationsDatei datei = new InformationsDatei(1, 1, "User", LocalDateTime.now(), 
                "Test", null, "file.pdf", "test.pdf", InformationsDatei.DateiTyp.PDF);

        Path tempFile = Files.createTempFile("test-download", ".pdf");
        Files.writeString(tempFile, "Test Content");

        when(dateiService.findById(1)).thenReturn(datei);
        when(dateiService.getFilePath("file.pdf")).thenReturn(tempFile);

        mockMvc.perform(get("/betreuer/1/dateien/1/download"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.pdf\""));

        Files.deleteIfExists(tempFile);
    }

    @Test
    @DisplayName("Datei löschen sollte funktionieren")
    void test_deleteDatei() throws Exception {
        InformationsDatei datei = new InformationsDatei(1, 1, "User", LocalDateTime.now(), 
                "Test", null, "file.pdf", "test.pdf", InformationsDatei.DateiTyp.PDF);
        when(dateiService.findById(1)).thenReturn(datei);

        mockMvc.perform(post("/betreuer/1/dateien/1/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/betreuer/1"));

        verify(dateiService).deleteById(1);
    }
}
