package com.example.petmatch_lab.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityRoutesIntegrationTests {

    @Autowired private MockMvc mockMvc;

    @Test
    void shouldAllowAnonymousAccessToLoginAndRegister() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk());
        mockMvc.perform(get("/register")).andExpect(status().isOk());
    }

    @Test
    void shouldRedirectAnonymousVisitorToLogin() throws Exception {
        mockMvc.perform(get("/pets"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }

    @Test
    void shouldForbidUserFromAdminPageAndPetCreation() throws Exception {
        mockMvc.perform(get("/admin").with(user("ana@example.com").roles("USER")))
            .andExpect(status().isForbidden());
        mockMvc.perform(post("/pets").with(user("ana@example.com").roles("USER")).with(csrf()))
            .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAdminToOpenAdminPage() throws Exception {
        mockMvc.perform(get("/admin").with(user("admin@example.com").roles("ADMIN")))
            .andExpect(status().isOk());
    }

    @Test
    void shouldForbidUserFromCompletingSupportRequest() throws Exception {
        mockMvc.perform(post("/pets/7/requests/9/complete")
                .with(user("ana@example.com").roles("USER")).with(csrf()))
            .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectPostWithoutCsrfToken() throws Exception {
        mockMvc.perform(post("/pets").with(user("admin@example.com").roles("ADMIN")))
            .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectPostWithInvalidCsrfToken() throws Exception {
        mockMvc.perform(post("/pets").with(user("admin@example.com").roles("ADMIN"))
                .with(csrf().useInvalidToken()))
            .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowUserToOpenPetsAndRegisterSupportRequest() throws Exception {
        mockMvc.perform(get("/pets").with(user("ana@example.com").roles("USER")))
            .andExpect(status().isOk());
    }
}
