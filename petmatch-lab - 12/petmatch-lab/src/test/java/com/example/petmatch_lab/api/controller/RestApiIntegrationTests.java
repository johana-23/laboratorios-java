package com.example.petmatch_lab.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.petmatch_lab.model.AppUser;
import com.example.petmatch_lab.model.Pet;
import com.example.petmatch_lab.model.RequestStatus;
import com.example.petmatch_lab.model.SupportRequest;
import com.example.petmatch_lab.model.SupportType;
import com.example.petmatch_lab.repository.AppUserRepository;
import com.example.petmatch_lab.repository.PetRepository;
import com.example.petmatch_lab.repository.SupportRequestRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestApiIntegrationTests {

    private static final String USER_EMAIL = "api-user-test@example.com";
    private static final String ADMIN_EMAIL = "api-admin-test@example.com";
    private static final String PASSWORD = "ExamplePassword123!";

    @Autowired private MockMvc mockMvc;
    @Autowired private AppUserRepository userRepository;
    @Autowired private PetRepository petRepository;
    @Autowired private SupportRequestRepository requestRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JdbcTemplate jdbcTemplate;

    @BeforeAll
    void prepareAccounts() {
        userRepository.findByEmail(USER_EMAIL).ifPresent(userRepository::delete);
        userRepository.findByEmail(ADMIN_EMAIL).ifPresent(userRepository::delete);

        userRepository.saveAndFlush(new AppUser(
            "API User", USER_EMAIL, passwordEncoder.encode(PASSWORD)
        ));

        AppUser admin = userRepository.saveAndFlush(new AppUser(
            "API Admin", ADMIN_EMAIL, passwordEncoder.encode(PASSWORD)
        ));

        jdbcTemplate.update(
            "UPDATE app_users SET role = 'ADMIN' WHERE id = ?",
            admin.getId()
        );
    }

    @BeforeEach
    void cleanPetData() {
        requestRepository.deleteAll();
        petRepository.deleteAll();
    }

    @Test
    void shouldRequireAuthenticationForApiRead() throws Exception {
        mockMvc.perform(get("/api/v1/pets"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectIncorrectBasicPassword() throws Exception {
        mockMvc.perform(get("/api/v1/pets")
                .with(httpBasic(USER_EMAIL, "WrongPassword123!")))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowRegisteredUserToReadPets() throws Exception {
        mockMvc.perform(get("/api/v1/pets")
                .with(httpBasic(USER_EMAIL, PASSWORD)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldRejectPetCreationForUserRole() throws Exception {
        mockMvc.perform(post("/api/v1/pets")
                .with(httpBasic(USER_EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"Kira","species":"Dog","age":4}
                    """))
            .andExpect(status().isForbidden());
        assertEquals(0, petRepository.count());
    }

    @Test
    void shouldCreatePetWithLocationForAdmin() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/pets")
                .with(httpBasic(ADMIN_EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"Kira","species":"Dog","age":4}
                    """))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.name").value("Kira"))
            .andExpect(jsonPath("$.age").value(4))
            .andReturn();

        String location = result.getResponse().getHeader("Location");
        assertNotNull(location);
        long petId = Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
        assertEquals("Kira", petRepository.findById(petId).orElseThrow().getName());
    }

    @Test
    void shouldReportInvalidPetFieldsAsJson() throws Exception {
        mockMvc.perform(post("/api/v1/pets")
                .with(httpBasic(ADMIN_EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"Kira","species":"Dog","age":-2}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("validation_failed"))
            .andExpect(jsonPath("$.fieldErrors.age").exists());
    }

    @Test
    void shouldReportMalformedJson() throws Exception {
        mockMvc.perform(post("/api/v1/pets")
                .with(httpBasic(ADMIN_EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Kira\""))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("invalid_json"));
    }

    @Test
    void shouldReturnJsonNotFoundForMissingPet() throws Exception {
        mockMvc.perform(get("/api/v1/pets/{id}", Long.MAX_VALUE)
                .with(httpBasic(USER_EMAIL, PASSWORD)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("not_found"));
    }

    @Test
    void shouldAllowUserToCreateSupportRequest() throws Exception {
        Pet pet = petRepository.saveAndFlush(new Pet("Nube", "Rabbit", 2, null));
        mockMvc.perform(post("/api/v1/pets/{id}/requests", pet.getId())
                .with(httpBasic(USER_EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"type":"WALK","description":"Needs a walk."}
                    """))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.status").value("OPEN"))
            .andExpect(jsonPath("$.petId").value(pet.getId().intValue()));
    }

    @Test
    void shouldRejectSecondOpenSupportRequest() throws Exception {
        Pet pet = petRepository.saveAndFlush(new Pet("Luna", "Dog", 3, null));
        String body = """
            {"type":"WALK","description":"Needs a walk."}
            """;

        mockMvc.perform(post("/api/v1/pets/{id}/requests", pet.getId())
                .with(httpBasic(USER_EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/pets/{id}/requests", pet.getId())
                .with(httpBasic(USER_EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value("business_conflict"));

        assertEquals(1, requestRepository.count());
    }

    @Test
    void shouldRejectUnknownSupportType() throws Exception {
        Pet pet = petRepository.saveAndFlush(new Pet("Milo", "Cat", 2, null));
        mockMvc.perform(post("/api/v1/pets/{id}/requests", pet.getId())
                .with(httpBasic(USER_EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"type":"VETERINARY","description":"Needs help."}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("invalid_json"));
    }

    @Test
    void shouldRejectRequestUnderAnotherPet() throws Exception {
        Pet first = petRepository.saveAndFlush(new Pet("First", "Dog", 2, null));
        Pet second = petRepository.saveAndFlush(new Pet("Second", "Cat", 3, null));
        SupportRequest request = requestRepository.saveAndFlush(
            new SupportRequest(first, SupportType.WALK, "Needs a walk.")
        );

        mockMvc.perform(get(
                "/api/v1/pets/{petId}/requests/{requestId}",
                second.getId(), request.getId()
            ).with(httpBasic(USER_EMAIL, PASSWORD)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("not_found"));
    }

    @Test
    void shouldCompleteOpenRequestWithoutResponseBody() throws Exception {
        Pet pet = petRepository.saveAndFlush(new Pet("Luna", "Dog", 3, null));
        SupportRequest request = requestRepository.saveAndFlush(
            new SupportRequest(pet, SupportType.COMPANY, "Needs company.")
        );

        mockMvc.perform(post(
                "/api/v1/pets/{petId}/requests/{requestId}/complete",
                pet.getId(), request.getId()
            ).with(httpBasic(ADMIN_EMAIL, PASSWORD)))
            .andExpect(status().isNoContent())
            .andExpect(content -> assertEquals(0, content.getResponse().getContentAsByteArray().length));

        assertEquals(RequestStatus.COMPLETED,
            requestRepository.findById(request.getId()).orElseThrow().getStatus());
    }

    @Test
    void shouldRejectCompletingRequestForUserRole() throws Exception {
        Pet pet = petRepository.saveAndFlush(new Pet("Luna", "Dog", 3, null));
        SupportRequest request = requestRepository.saveAndFlush(
            new SupportRequest(pet, SupportType.WALK, "Needs a walk.")
        );

        mockMvc.perform(post(
                "/api/v1/pets/{petId}/requests/{requestId}/complete",
                pet.getId(), request.getId()
            ).with(httpBasic(USER_EMAIL, PASSWORD)))
            .andExpect(status().isForbidden());

        assertEquals(RequestStatus.OPEN,
            requestRepository.findById(request.getId()).orElseThrow().getStatus());
    }

    @Test
    void shouldRejectCompletingCancelledRequest() throws Exception {
        Pet pet = petRepository.saveAndFlush(new Pet("Kira", "Dog", 4, null));
        SupportRequest request = new SupportRequest(
            pet, SupportType.FEEDING, "Needs feeding."
        );
        request.cancel();
        request = requestRepository.saveAndFlush(request);

        mockMvc.perform(post(
                "/api/v1/pets/{petId}/requests/{requestId}/complete",
                pet.getId(), request.getId()
            ).with(httpBasic(ADMIN_EMAIL, PASSWORD)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value("business_conflict"));
    }

    @Test
    void shouldRejectCompletingCompletedRequest() throws Exception {
        Pet pet = petRepository.saveAndFlush(new Pet("Maya", "Dog", 3, null));
        SupportRequest request = new SupportRequest(
            pet, SupportType.FEEDING, "Needs feeding."
        );
        request.complete();
        request = requestRepository.saveAndFlush(request);

        mockMvc.perform(post(
                "/api/v1/pets/{petId}/requests/{requestId}/complete",
                pet.getId(), request.getId()
            ).with(httpBasic(ADMIN_EMAIL, PASSWORD)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value("business_conflict"));
    }

    @Test
    void shouldCancelOpenRequestAsAdmin() throws Exception {
        Pet pet = petRepository.saveAndFlush(new Pet("Toby", "Dog", 2, null));
        SupportRequest request = requestRepository.saveAndFlush(
            new SupportRequest(pet, SupportType.WALK, "Short walk.")
        );

        mockMvc.perform(post(
                "/api/v1/pets/{petId}/requests/{requestId}/cancel",
                pet.getId(), request.getId()
            ).with(httpBasic(ADMIN_EMAIL, PASSWORD)))
            .andExpect(status().isNoContent());

        assertEquals(RequestStatus.CANCELLED,
            requestRepository.findById(request.getId()).orElseThrow().getStatus());
    }

    @Test
    void shouldReplaceRequestUsingExistingTransactionalService() throws Exception {
        Pet pet = petRepository.saveAndFlush(new Pet("Toby", "Dog", 2, null));
        SupportRequest previous = requestRepository.saveAndFlush(
            new SupportRequest(pet, SupportType.WALK, "Short walk.")
        );

        mockMvc.perform(put(
                "/api/v1/pets/{petId}/requests/{requestId}/replace",
                pet.getId(), previous.getId()
            ).with(httpBasic(ADMIN_EMAIL, PASSWORD))
             .contentType(MediaType.APPLICATION_JSON)
             .content("""
                {"type":"COMPANY","description":"Stay together."}
                """))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.status").value("OPEN"));

        assertEquals(RequestStatus.CANCELLED,
            requestRepository.findById(previous.getId()).orElseThrow().getStatus());
        assertEquals(1, requestRepository.findByPet_IdOrderByIdDesc(pet.getId()).stream()
            .filter(SupportRequest::isOpen).count());
    }

    @Test
    void shouldUpdatePetAsAdmin() throws Exception {
        Pet pet = petRepository.saveAndFlush(new Pet("Nina", "Cat", 4, null));

        mockMvc.perform(put("/api/v1/pets/{petId}", pet.getId())
                .with(httpBasic(ADMIN_EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"Nina Updated","species":"Cat","age":5}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Nina Updated"));

        assertEquals("Nina Updated", petRepository.findById(pet.getId()).orElseThrow().getName());
    }

    @Test
    void shouldDeletePetWithoutRequestHistory() throws Exception {
        Pet pet = petRepository.saveAndFlush(new Pet("Nina", "Cat", 4, null));

        mockMvc.perform(delete("/api/v1/pets/{id}", pet.getId())
                .with(httpBasic(ADMIN_EMAIL, PASSWORD)))
            .andExpect(status().isNoContent());

        assertTrue(!petRepository.existsById(pet.getId()));
    }

    @Test
    void shouldRejectDeletingPetWithRequestHistory() throws Exception {
        Pet pet = petRepository.saveAndFlush(new Pet("Nina", "Cat", 4, null));
        requestRepository.saveAndFlush(
            new SupportRequest(pet, SupportType.COMPANY, "Stay nearby.")
        );

        mockMvc.perform(delete("/api/v1/pets/{id}", pet.getId())
                .with(httpBasic(ADMIN_EMAIL, PASSWORD)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value("business_conflict"));

        assertTrue(petRepository.existsById(pet.getId()));
    }

    @Test
    void shouldKeepCsrfProtectionOnWebForms() throws Exception {
        mockMvc.perform(post("/pets")
                .with(user(ADMIN_EMAIL).roles("ADMIN"))
                .param("name", "Kira")
                .param("species", "Dog")
                .param("age", "4"))
            .andExpect(status().isForbidden());
    }
}
