package com.project.store.security;

import com.project.store.domain.User;
import com.project.store.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(
            UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @ModelAttribute(name = "registrationDTO")
    public RegistrationDTO regForm() {
        return new RegistrationDTO();
    }

    @PostMapping
    public String processRegistration(@Valid @ModelAttribute("registrationDTO") RegistrationDTO registrationDTO, BindingResult bindingResult) {
        User foundUser = userRepo.findByUsername(registrationDTO.getUsername());
        if (foundUser != null) {
            bindingResult.addError(new FieldError("registrationForm", "userExists", "User with this nickname already exists!"));
            return "registration";
        }
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            bindingResult.addError(new FieldError("registrationForm", "confirm", "Entered passwords don't match"));
            return "registration";
        }

        userRepo.save(registrationDTO.toUser(passwordEncoder));
        return "redirect:/login";
    }

}