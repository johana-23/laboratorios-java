package com.example.petmatch_lab.dto;

import com.example.petmatch_lab.model.UserRole;

public class ProfileView {

    private final String email;
    private final String displayName;
    private final UserRole role;

    public ProfileView(String email, String displayName, UserRole role) {
        this.email = email;
        this.displayName = displayName;
        this.role = role;
    }

    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public UserRole getRole() { return role; }
}
