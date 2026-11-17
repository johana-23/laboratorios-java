package com.example.petmatch_lab.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.petmatch_lab.dto.RegistrationForm;
import com.example.petmatch_lab.exception.DuplicateEmailException;
import com.example.petmatch_lab.model.AppUser;
import com.example.petmatch_lab.model.UserRole;
import com.example.petmatch_lab.repository.AppUserRepository;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTests {

    @Mock private AppUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RegistrationService service;

    @BeforeEach
    void setUp() {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        service = new RegistrationService(userRepository, passwordEncoder);
    }

    @Test
    void shouldSaveNormalizedEmailHashedPasswordAndUserRole() {
        RegistrationForm form = form("  Ana  ", " ANA@EXAMPLE.COM ", "ExamplePassword123!");
        when(userRepository.existsByEmail("ana@example.com")).thenReturn(false);
        when(userRepository.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppUser saved = service.register(form);

        assertEquals("Ana", saved.getDisplayName());
        assertEquals("ana@example.com", saved.getEmail());
        assertEquals(UserRole.USER, saved.getRole());
        org.junit.jupiter.api.Assertions.assertTrue(saved.isEnabled());
        assertFalse("ExamplePassword123!".equals(saved.getPasswordHash()));
        org.junit.jupiter.api.Assertions.assertTrue(passwordEncoder.matches("ExamplePassword123!", saved.getPasswordHash()));
        verify(userRepository).save(any(AppUser.class));
    }

    @Test
    void shouldRejectDuplicateEmail() {
        when(userRepository.existsByEmail("ana@example.com")).thenReturn(true);
        assertThrows(DuplicateEmailException.class, () -> service.register(form("Ana", "ana@example.com", "ExamplePassword123!")));
        verify(userRepository, never()).save(any(AppUser.class));
    }

    @Test
    void shouldRejectPasswordMismatch() {
        RegistrationForm form = form("Ana", "ana@example.com", "ExamplePassword123!");
        form.setConfirmPassword("DifferentPassword123!");
        assertThrows(IllegalArgumentException.class, () -> service.register(form));
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldRejectShortPasswordBeforeUsingRepository() {
        RegistrationForm form = form("Ana", "ana@example.com", "short");
        assertThrows(IllegalArgumentException.class, () -> service.register(form));
        verifyNoInteractions(userRepository);
    }

    private RegistrationForm form(String name, String email, String password) {
        RegistrationForm form = new RegistrationForm();
        form.setDisplayName(name);
        form.setEmail(email);
        form.setPassword(password);
        form.setConfirmPassword(password);
        return form;
    }
}
