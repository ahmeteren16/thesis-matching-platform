package controller;

import java.util.List;

import builder.BetreuerProfileBuilder;
import builder.ThemaBuilder;
import com.lorem_ipsum.thesis.ThesisApplication;
import com.lorem_ipsum.thesis.controller.MatchingController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.lorem_ipsum.thesis.domain.BetreuerProfile;
import com.lorem_ipsum.thesis.domain.Thema;
import com.lorem_ipsum.thesis.service.BetreuerService;
import com.lorem_ipsum.thesis.service.ThemaService;

@WebMvcTest(MatchingController.class)
@WithMockUser
@ContextConfiguration(classes = ThesisApplication.class)
public class MatchingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private BetreuerService betreuerService;

    @MockitoBean
    private ThemaService themaService;

    @Test
    @DisplayName("GET /matching sollte die Matching-Seite anzeigen")
    void test_showMatching() throws Exception {
        BetreuerProfile betreuer = BetreuerProfileBuilder.aBetreuer()
                .withGithubId("github1")
                .withName("Max Mustermann")
                .withEmail("max@example")
                .withTags(List.of("Java", "Spring"))
                .buildwithId(1);

        Thema thema = ThemaBuilder.aThema()
                .withTitle("Thema1")
                .withVoraussetzungen(List.of("Java"))
                .withBetreuerId(1)
                .build();
        
        List<BetreuerProfile> betreuerList = List.of(betreuer);
        List<Thema> themenList = List.of(thema);

        when(betreuerService.findById(anyInt())).thenReturn(betreuer);
        when(themaService.findByVoraussetzungen("Java")).thenReturn(themenList);

        mockMvc.perform(get("/matching").param("voraussetzung", "Java"))
                .andExpect(status().isOk())
                .andExpect(view().name("matching/index"))
                .andExpect(model().attribute("themen", themenList))
                .andExpect(model().attribute("betreuer", betreuerList))
                .andExpect(model().attribute("voraussetzung", "Java"));
    }

    @Test
    @DisplayName("POST /matching/assign sollte ein Thema einem Betreuer zuweisen")
    void test_assignThemaToBetreuer() throws Exception {
        BetreuerProfile betreuer = BetreuerProfileBuilder.aBetreuer()
                .withGithubId("github1")
                .withName("Max Mustermann")
                .withEmail("max@example")
                .withTags(List.of("Java", "Spring"))
                .buildwithId(1);

        Thema thema = ThemaBuilder.aThema()
                .withTitle("Thema1")
                .build();

        mockMvc.perform(post("/matching/assign")
                        .with(csrf())
                        .param("titel1", thema.getTitel())
                        .param("betreuerId", betreuer.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/matching"));

        verify(themaService).assignToBetreuer(thema.getTitel(), betreuer.getId());
    }
    @Test
    @DisplayName("GET /themen sollte alle Themen anzeigen")
    void test_01() throws Exception {
        Thema thema = ThemaBuilder.aThema()
                .withTitle("Thema1")
                .build();

        List<Thema> themenList = List.of(thema);


        when(themaService.findAll()).thenReturn(themenList);

        mockMvc.perform(get("/themen"))
                .andExpect(status().isOk())
                .andExpect(view().name("matching/themen"))
                .andExpect(model().attributeExists("themen"))
                .andExpect(model().attribute("themen", themenList));

    }

    @Test
    @DisplayName("POST /themen/add sollte ein Thema hinzufügen")
    void test_02() throws Exception {
        Thema thema = ThemaBuilder.aThema()
                .withTitle("Thema1")
                .build();



        mockMvc.perform(post("/themen/add")
                .with(csrf())
                .param("titel", thema.getTitel()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/themen"));

        verify(themaService).save(any());


    }

}
