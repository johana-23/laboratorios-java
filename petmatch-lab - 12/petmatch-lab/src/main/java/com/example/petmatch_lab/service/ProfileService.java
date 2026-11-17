package com.example.petmatch_lab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.petmatch_lab.dto.ProfileView;
import com.example.petmatch_lab.exception.ResourceNotFoundException;
import com.example.petmatch_lab.model.AppUser;
import com.example.petmatch_lab.repository.AppUserRepository;

@Service
public class ProfileService {

    private final AppUserRepository userRepository;

    public ProfileService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public ProfileView findByEmail(String email) {
        AppUser user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("No se encontró la cuenta autenticada."));
        return new ProfileView(user.getEmail(), user.getDisplayName(), user.getRole());
    }
}
