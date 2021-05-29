package com.project.store.controller;

import com.project.store.domain.User;
import com.project.store.model.CartDTO;
import com.project.store.repository.ProductRepository;
import com.project.store.repository.TagRepository;
import com.project.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/product")
@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/all")
    public String getProductsPage(Model model) {
        populateProducts(model);
        return "products";
    }

    private void populateProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());
    }

    @ModelAttribute("cart")
    public CartDTO getCartNumber(Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName());
        if (currentUser != null) {
            CartDTO cartDTO = new CartDTO();
            cartDTO.setUserId(currentUser.getId());
            cartDTO.setProducts(currentUser.getProducts());
            return cartDTO;
        } else {
            return null;
        }
    }
}
