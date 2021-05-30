package com.project.store.security;

import com.project.store.domain.Role;
import com.project.store.domain.RoleEnum;
import com.project.store.domain.Tag;
import com.project.store.domain.User;
import com.project.store.repository.RoleRepository;
import com.project.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String redirectTo(Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName());
        Set<Role> userRoles = currentUser.getRoles();
        for (Role role : userRoles) {
            if (role.getName().equals(RoleEnum.ROLE_USER.name())) {
                return "redirect:/product/all";
            } else {
                return "redirect:/admin";
            }
        }
        return "redirect:/login";
    }
}
