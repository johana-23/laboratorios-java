package com.example.petmatch_lab.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.petmatch_lab.model.AppUser;
import com.example.petmatch_lab.repository.AppUserRepository;

@ExtendWith(MockitoExtension.class)
class DatabaseUserDetailsServiceTests {

    @Mock private AppUserRepository userRepository;

    @Test
    void shouldConvertStoredAccountToSpringSecurityUserDetails() {
        AppUser appUser = new AppUser("Ana", "ana@example.com", "{bcrypt}encoded-value");
        when(userRepository.findByEmail("ana@example.com")).thenReturn(Optional.of(appUser));

        var details = new DatabaseUserDetailsService(userRepository).loadUserByUsername(" ANA@EXAMPLE.COM ");

        assertEquals("ana@example.com", details.getUsername());
        assertEquals("{bcrypt}encoded-value", details.getPassword());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(details.isEnabled());
    }

    @Test
    void shouldRejectUnknownAccount() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
            () -> new DatabaseUserDetailsService(userRepository).loadUserByUsername("missing@example.com"));
    }
}
