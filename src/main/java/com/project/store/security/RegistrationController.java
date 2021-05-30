package com.project.store.security;

import com.project.store.domain.Role;
import com.project.store.domain.RoleEnum;
import com.project.store.domain.User;
import com.project.store.repository.RoleRepository;
import com.project.store.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepo;

    public RegistrationController(
            UserRepository userRepo, PasswordEncoder passwordEncoder, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
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

        User newUser = registrationDTO.toUser(passwordEncoder);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepo.findByName(RoleEnum.ROLE_USER.name()));
        newUser.setRoles(roles);
        userRepo.save(newUser);
        return "redirect:/login";
    }

}