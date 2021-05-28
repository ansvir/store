package com.project.store.security;

import com.project.store.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class RegistrationDTO {
    @NotBlank
    @Size(min = 2, max = 40)
    @Pattern(regexp = "^[A-Za-z0-9_]*$")
    private String username;
    @NotBlank
    @Size(min = 8, max = 40)
    @Pattern(regexp = "^[A-Za-z0-9!@#$%^&*()<>?\\-=+.]*$")
    private String password;
    @NotBlank
    @Size
    @Pattern(regexp = "^[A-Za-z0-9!@#$%^&*()<>?\\-=+.]*$")
    private String confirmPassword;
    @NotBlank
    @Email
    private String email;
    private boolean userExists;
    private boolean confirm;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(
                username, passwordEncoder.encode(password),
                email);
    }

}
