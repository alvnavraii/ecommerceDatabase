package com.ecommerce.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestPasswordController {
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/api/v1/auth/test-password")
    public String testPassword() {
        String rawPassword = "Temporal1!";
        String storedHash = "$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW";
        
        boolean matches = passwordEncoder.matches(rawPassword, storedHash);
        String newHash = passwordEncoder.encode(rawPassword);
        boolean newMatches = passwordEncoder.matches(rawPassword, newHash);
        
        return String.format("""
            Password matches: %b
            New hash: %s
            New hash matches: %b
            """, matches, newHash, newMatches);
    }
} 