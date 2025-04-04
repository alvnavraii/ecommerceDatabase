package com.ecommerce.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class TestPasswordEncoder {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void testPassword() {
        String rawPassword = "Temporal1!";
        String storedHash = "$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW";
        
        System.err.println("=====================================");
        System.err.println("TESTING PASSWORD ENCODER");
        System.err.println("Raw password: " + rawPassword);
        System.err.println("Stored hash: " + storedHash);
        
        boolean matches = passwordEncoder.matches(rawPassword, storedHash);
        System.err.println("Password matches: " + matches);
        
        String newHash = passwordEncoder.encode(rawPassword);
        System.err.println("New hash: " + newHash);
        
        boolean newMatches = passwordEncoder.matches(rawPassword, newHash);
        System.err.println("New hash matches: " + newMatches);
        System.err.println("=====================================");
    }
} 