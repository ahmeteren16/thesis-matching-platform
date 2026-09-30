package controller;

import java.util.List;
import java.util.Optional;

import builder.BetreuerProfileBuilder;
import com.lorem_ipsum.thesis.ThesisApplication;
import com.lorem_ipsum.thesis.config.AdminOnly;
import com.lorem_ipsum.thesis.config.AppUserService;
import com.lorem_ipsum.thesis.config.SecurityConfig;
import com.lorem_ipsum.thesis.controller.BetreuerController;
import com.lorem_ipsum.thesis.domain.repository.BetreuerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lorem_ipsum.thesis.domain.BetreuerProfile;
import com.lorem_ipsum.thesis.service.BetreuerService;
import com.lorem_ipsum.thesis.service.InformationsDateiService;

@WebMvcTest(BetreuerController.class)
@ContextConfiguration(classes = ThesisApplication.class)
@AutoConfigureMockMvc(addFilters = false)
class BetreuerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BetreuerService betreuerService;

    @MockitoBean
    private InformationsDateiService dateiService;

    @Test
    @DisplayName("GET /betreuer sollte die Betreuer-Liste anzeigen")
    void test_listBetreuer() throws Exception {

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


        List<BetreuerProfile> betreuer = List.of(betreuer1, betreuer2);

        when(betreuerService.getAllBetreuer()).thenReturn(betreuer);
            
        mockMvc.perform(get("/betreuer"))
                .andExpect(status().isOk())
                .andExpect(view().name("betreuer/liste"))
                .andExpect(model().attributeExists("betreuer"))
                .andExpect(model().attribute("betreuer", betreuer));
    }

    @Test
    @DisplayName("GET /betreuer/details/{id} sollte einen einzelnen Betreuer anzeigen")
    void test_showBetreuer() throws Exception {
        BetreuerProfile betreuer = BetreuerProfileBuilder.aBetreuer()
                .withGithubId("github1")
                .withName("Max Mustermann")
                .withEmail("max@example")
                .withTags(List.of("Java", "Spring"))
                .buildwithId(1);

        when(betreuerService.findById(any())).thenReturn(betreuer);

        mockMvc.perform(get("/betreuer/details/{id}", betreuer.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("betreuer/details"))
                .andExpect(model().attributeExists("betreuer"))
                .andExpect(model().attribute("betreuer", betreuer));
    }

    @Test
    @DisplayName("GET /betreuer/add kann nur von Admins aufgerufen werden")
    @WithMockUser(roles = "ADMIN")
    void test_addBetreuer() throws Exception {

        mockMvc.perform(get("/betreuer/add"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /betreuer/add sollte einen Betreuer hinzufügen")
    void test_addBetreuer_02() throws Exception {
        BetreuerProfile betreuer = BetreuerProfileBuilder.aBetreuer()
                .withGithubId("github1")
                .withName("Max Mustermann")
                .withEmail("max@example")
                .withTags(List.of("Java", "Spring"))
                .buildwithId(1);

        when(betreuerService.findByEmail(anyString()))
                .thenThrow(new RuntimeException());



        mockMvc.perform(post("/betreuer/add")
                        .with(csrf())
                        .param("name",betreuer.getName())
                        .param("githubID",betreuer.getGithubID())
                        .param("email",betreuer.getEmail()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/betreuer"));

        verify(betreuerService).save(any());
    }

    @Test
    @DisplayName("POST /betreuer/add gibt einen Fehler zurück, wenn die E-Mail bereits existiert")
    void test_addBetreuer_03() throws Exception {
        BetreuerProfile betreuer = BetreuerProfileBuilder.aBetreuer()
                .withGithubId("github1")
                .withName("Max Mustermann")
                .withEmail("max@example")
                .withTags(List.of("Java", "Spring"))
                .buildwithId(1);

        when(betreuerService.findByEmail("max@example"))
                .thenReturn(betreuer);


        mockMvc.perform(post("/betreuer/add")
                        .with(csrf())
                        .param("name",betreuer.getName())
                        .param("githubID",betreuer.getGithubID())
                        .param("email","max@example"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/betreuer/add"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attribute("errorMessage","Mit dieser E-Mail-Adresse ist bereits ein Betreuer registriert"));



    }




}
