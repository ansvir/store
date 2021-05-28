package com.project.store.controller;

import com.project.store.domain.Product;
import com.project.store.domain.User;
import com.project.store.model.Cart;
import com.project.store.repository.ProductRepository;
import com.project.store.repository.TagRepository;
import com.project.store.repository.UserRepository;
import com.project.store.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public Cart getCartNumber(Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName());
        if (currentUser != null) {
            Cart cart = new Cart();
            cart.setUserId(currentUser.getId());
            cart.setProducts(currentUser.getProducts());
            return cart;
        } else {
            return null;
        }
    }
}
