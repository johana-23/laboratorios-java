package com.example.petmatch_lab.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.example.petmatch_lab.model.AppUser;
import com.example.petmatch_lab.repository.AppUserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerIntegrationTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private AppUserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void shouldRedirectAnonymousVisitorToLogin() throws Exception {
        mockMvc.perform(get("/profile")).andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldReturnControlledNotFoundWhenAuthenticatedAccountNoLongerExists() throws Exception {
        mockMvc.perform(get("/profile").with(user("deleted-account@example.com").roles("USER")))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldShowOnlyAuthenticatedUsersPublicProfileData() throws Exception {
        String email = "profile-test@example.com";
        userRepository.findByEmail(email).ifPresent(userRepository::delete);
        userRepository.saveAndFlush(new AppUser("Profile User", email, passwordEncoder.encode("ExamplePassword123!")));

        mockMvc.perform(get("/profile").with(user(email).roles("USER")))
            .andExpect(status().isOk())
            .andExpect(view().name("profile"))
            .andExpect(model().attributeExists("profile"))
            .andExpect(model().attributeDoesNotExist("passwordHash"));
    }
}
