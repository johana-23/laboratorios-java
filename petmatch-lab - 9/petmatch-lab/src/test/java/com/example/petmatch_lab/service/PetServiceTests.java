package com.example.petmatch_lab.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.petmatch_lab.dto.PetForm;
import com.example.petmatch_lab.model.Pet;
import com.example.petmatch_lab.repository.PetRepository;

@ExtendWith(MockitoExtension.class)
class PetServiceTests {

    @Mock
    private PetRepository repository;

    private PetService service;

    @BeforeEach
    void setUp() {
        service = new PetService(repository);
    }

    @Test
    void shouldNormalizeFieldsBeforeSaving() {
        PetForm form = newForm(" Kira ", " Perro ", 4);
        form.setDescription(" Muy juguetona. ");
        when(repository.save(any(Pet.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Pet created = service.create(form);

        assertEquals("Kira", created.getName());
        assertEquals("Perro", created.getSpecies());
        assertEquals("Muy juguetona.", created.getDescription());
        verify(repository).save(any(Pet.class));
    }

    @Test
    void shouldRejectInvalidAgeBeforeCallingRepository() {
        PetForm form = newForm("Kira", "Perro", -1);

        assertThrows(IllegalArgumentException.class, () -> service.create(form));

        verify(repository, never()).save(any(Pet.class));
    }

    @Test
    void shouldReturnEmptyWhenUpdatingMissingPet() {
        PetForm form = newForm("Kira", "Gato", 5);
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertTrue(service.update(999L, form).isEmpty());
        verify(repository, never()).save(any(Pet.class));
    }

    @Test
    void shouldUpdateExistingPetWithoutCreatingAnotherOne() {
        PetForm form = newForm("Milo", "Gato", 5);
        Pet existing = new Pet("Milo", "Gato", 2, null);
        when(repository.findById(7L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Pet.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Pet updated = service.update(7L, form).orElseThrow();

        assertEquals(5, updated.getAge());
        verify(repository).save(existing);
    }

    @Test
    void shouldReturnFalseWhenDeletingMissingPet() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertFalse(service.delete(999L));
        verify(repository, never()).delete(any(Pet.class));
    }

    @Test
    void shouldDeleteExistingPet() {
        Pet existing = new Pet("Luna", "Perro", 3, null);
        when(repository.findById(8L)).thenReturn(Optional.of(existing));

        assertTrue(service.delete(8L));
        verify(repository).delete(existing);
    }

    @Test
    void shouldUseRepositorySearchForSpecies() {
        Pet ayla = new Pet("Ayla", "Perro", 2, null);
        when(repository.findBySpeciesIgnoreCaseOrderByNameAsc("Perro"))
            .thenReturn(List.of(ayla));

        List<Pet> result = service.findBySpecies(" Perro ");

        assertEquals(1, result.size());
        assertEquals("Ayla", result.get(0).getName());
        verify(repository).findBySpeciesIgnoreCaseOrderByNameAsc("Perro");
    }

    private PetForm newForm(String name, String species, Integer age) {
        PetForm form = new PetForm();
        form.setName(name);
        form.setSpecies(species);
        form.setAge(age);
        return form;
    }
}
