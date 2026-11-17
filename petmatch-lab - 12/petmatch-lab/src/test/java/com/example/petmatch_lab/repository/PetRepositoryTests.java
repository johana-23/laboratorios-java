package com.example.petmatch_lab.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.petmatch_lab.model.Pet;

import jakarta.persistence.EntityManager;

@DataJpaTest
class PetRepositoryTests {

    @Autowired
    private PetRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldGenerateIdentifierWhenSavingNewPet() {
        Pet pet = new Pet("Kira", "Perro", 4, "Muy juguetona.");

        Pet saved = repository.saveAndFlush(pet);

        assertNotNull(saved.getId());
        assertEquals("Kira", saved.getName());
    }

    @Test
    void shouldFindPersistedPetByIdentifier() {
        Pet saved = repository.saveAndFlush(
            new Pet("Luna", "Perro", 3, "Disfruta los paseos.")
        );
        Long id = saved.getId();
        entityManager.clear();

        Pet found = repository.findById(id).orElseThrow();

        assertEquals("Luna", found.getName());
        assertEquals(3, found.getAge());
    }

    @Test
    void shouldPersistChangesToExistingPet() {
        Pet saved = repository.saveAndFlush(
            new Pet("Milo", "Gato", 2, null)
        );
        Long id = saved.getId();

        saved.updateDetails("Milo", "Gato", 5, null);
        repository.saveAndFlush(saved);
        entityManager.clear();

        Pet found = repository.findById(id).orElseThrow();
        assertEquals(5, found.getAge());
    }

    @Test
    void shouldDeletePersistedPet() {
        Pet saved = repository.saveAndFlush(
            new Pet("Nube", "Conejo", 1, null)
        );
        Long id = saved.getId();

        repository.delete(saved);
        repository.flush();
        entityManager.clear();

        assertFalse(repository.findById(id).isPresent());
    }

    @Test
    void shouldReturnEmptyWhenIdentifierDoesNotExist() {
        assertTrue(repository.findById(Long.MAX_VALUE).isEmpty());
    }

    @Test
    void shouldFindSpeciesIgnoringCaseAndOrderByName() {
        repository.saveAll(List.of(
            new Pet("Toby", "Perro", 2, null),
            new Pet("Ayla", "Perro", 1, null),
            new Pet("Milo", "Gato", 3, null)
        ));
        repository.flush();

        List<Pet> dogs = repository.findBySpeciesIgnoreCaseOrderByNameAsc("PERRO");

        assertEquals(2, dogs.size());
        assertEquals("Ayla", dogs.get(0).getName());
        assertEquals("Toby", dogs.get(1).getName());
    }
}
