package com.example.petmatch_lab.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.petmatch_lab.model.Pet;
import com.example.petmatch_lab.repository.PetRepository;
import com.example.petmatch_lab.repository.SupportRequestRepository;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin@example.com", roles = "ADMIN")
class PetControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private SupportRequestRepository requestRepository;

    @BeforeEach
    void cleanTestData() {
        requestRepository.deleteAll();
        petRepository.deleteAll();
    }

    @Test
    void shouldRenderCreateForm() throws Exception {
        mockMvc.perform(get("/pets/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/form"))
            .andExpect(model().attributeExists("petForm"));
    }

    @Test
    void shouldRejectInvalidPetFormWithoutSaving() throws Exception {
        mockMvc.perform(post("/pets")
                .with(csrf())
                .param("name", "")
                .param("species", "   ")
                .param("age", "-1")
                .param("description", "Prueba"))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/form"))
            .andExpect(model().attributeHasFieldErrors(
                "petForm", "name", "species", "age"));

        org.junit.jupiter.api.Assertions.assertEquals(0, petRepository.count());
    }

    @Test
    void shouldRedirectAfterValidCreation() throws Exception {
        mockMvc.perform(post("/pets")
                .with(csrf())
                .param("name", "Kira")
                .param("species", "Perro")
                .param("age", "4")
                .param("description", "Juguetona"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/pets/*"));

        org.junit.jupiter.api.Assertions.assertEquals(1, petRepository.count());
    }

    @Test
    void shouldFilterPetsByKnownSpecies() throws Exception {
        petRepository.save(new Pet("Milo", "Gato", 2, null));
        petRepository.save(new Pet("Luna", "Perro", 3, null));

        mockMvc.perform(get("/pets").param("species", "GATO"))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/list"))
            .andExpect(model().attribute("pets", org.hamcrest.Matchers.hasSize(1)));
    }

    @Test
    void shouldShowEmptyResultWhenSpeciesDoesNotExist() throws Exception {
        petRepository.save(new Pet("Milo", "Gato", 2, null));

        mockMvc.perform(get("/pets").param("species", "Ave"))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/list"))
            .andExpect(model().attribute("pets", org.hamcrest.Matchers.empty()));
    }

    @Test
    void shouldReturnNotFoundWhenEditingMissingPet() throws Exception {
        mockMvc.perform(get("/pets/99999999/edit"))
            .andExpect(status().isNotFound());
    }


    @Test
    void shouldReturnConflictWhenDeletingPetWithSupportRequests() throws Exception {
        Pet pet = petRepository.saveAndFlush(
            new Pet("Luna", "Perro", 3, null)
        );
        requestRepository.saveAndFlush(
            new com.example.petmatch_lab.model.SupportRequest(
                pet,
                com.example.petmatch_lab.model.SupportType.WALK,
                "Necesita un paseo"
            )
        );

        mockMvc.perform(post("/pets/{id}/delete", pet.getId()).with(csrf()))
            .andExpect(status().isConflict())
            .andExpect(view().name("errors/status"));
    }

    @Test
    void shouldReturnNotFoundWhenDeletingMissingPet() throws Exception {
        mockMvc.perform(post("/pets/99999999/delete").with(csrf()))
            .andExpect(status().isNotFound());
    }
}
