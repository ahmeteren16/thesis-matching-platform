package controller;

import com.lorem_ipsum.thesis.ThesisApplication;
import com.lorem_ipsum.thesis.controller.ThesisController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ThesisController.class)
@WithMockUser
@ContextConfiguration(classes = ThesisApplication.class)
class ThesisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET / sollte die Home-Seite anzeigen")
    void test_home() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    @DisplayName("GET /home sollte die Home-Seite anzeigen")
    void test_homepage() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    @DisplayName("GET /adminseite sollte die Admin-Seite anzeigen (mit Admin-Berechtigung)")
    @WithMockUser(roles = "ADMIN")
    void test_adminseite() throws Exception {
        mockMvc.perform(get("/adminseite"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/adminseite"));
    }
}
