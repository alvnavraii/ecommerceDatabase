package com.ecommerce.utils;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordHashTest {
    @Test
    public void testPasswordHash() {
        String password = "Temporal1!";
        String storedHash = "$2a$10$qWLU5d1N4vfRqh1LfMYRyulF5EH8..X/6i8VHx4KFsVjE5hbRgdxe";
        
        boolean matches = BCrypt.checkpw(password, storedHash);
        assertTrue(matches, "El hash debería coincidir con la contraseña 'Temporal1!'");
    }
}
