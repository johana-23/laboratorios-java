package com.example.petmatch_lab.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.petmatch_lab.model.Pet;
import com.example.petmatch_lab.model.SupportRequest;
import com.example.petmatch_lab.model.SupportType;
import com.example.petmatch_lab.repository.PetRepository;
import com.example.petmatch_lab.repository.SupportRequestRepository;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user@example.com", roles = "USER")
class SupportRequestControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private SupportRequestRepository requestRepository;

    @Test
    void shouldReturnNotFoundForUnknownPet() throws Exception {
        mockMvc.perform(get("/pets/{petId}/requests", Long.MAX_VALUE))
            .andExpect(status().isNotFound())
            .andExpect(view().name("errors/status"));
    }

    @Test
    void shouldRejectInvalidRequestForm() throws Exception {
        Pet pet = petRepository.saveAndFlush(
            new Pet("Kira", "Dog", 4, null)
        );

        mockMvc.perform(post("/pets/{petId}/requests", pet.getId())
                .with(csrf())
                .param("type", "")
                .param("description", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("requests/form"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void shouldReturnConflictForInvalidStateTransition() throws Exception {
        Pet pet = petRepository.saveAndFlush(
            new Pet("Nube", "Rabbit", 1, null)
        );
        SupportRequest request = new SupportRequest(
            pet, SupportType.COMPANY, "Needs company"
        );
        request.cancel();
        SupportRequest saved = requestRepository.saveAndFlush(request);

        mockMvc.perform(post(
                "/pets/{petId}/requests/{requestId}/complete",
                pet.getId(), saved.getId()
            ).with(csrf()))
            .andExpect(status().isConflict())
            .andExpect(view().name("errors/status"));
    }
}
