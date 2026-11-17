package com.example.petmatch_lab.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.petmatch_lab.model.AppUser;

@DataJpaTest
class AppUserRepositoryTests {

    @Autowired private AppUserRepository repository;

    @Test
    void shouldPersistAndFindAccountByEmail() {
        AppUser saved = repository.saveAndFlush(new AppUser("Ana", "ana@example.com", "{bcrypt}hash"));
        var found = repository.findByEmail("ana@example.com").orElseThrow();
        assertEquals(saved.getId(), found.getId());
        assertTrue(repository.existsByEmail("ana@example.com"));
    }
}
