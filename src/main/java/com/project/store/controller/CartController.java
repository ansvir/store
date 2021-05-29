package com.project.store.controller;

import com.project.store.domain.Product;
import com.project.store.domain.User;
import com.project.store.model.AddToCartDTO;
import com.project.store.model.CartDTO;
import com.project.store.model.RemoveFromCartDTO;
import com.project.store.repository.ProductRepository;
import com.project.store.repository.TagRepository;
import com.project.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RequestMapping("/cart")
@Controller
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    public String getCartPage(Model model) {
        model.addAttribute("tags", tagRepository.findAll());
        return "cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public void addToCart(@RequestBody AddToCartDTO addToCartDTO, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName());
        Optional<Product> addableProduct = productRepository.findById(addToCartDTO.getProductId());
        if (addableProduct.isPresent()) {
            currentUser.addProduct(addableProduct.get());
            userRepository.save(currentUser);
        }
    }

    @GetMapping("/quantity")
    @ResponseBody
    public String getCartQuantity(Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName());
        return String.valueOf(currentUser.getProducts().size());
    }

    @ModelAttribute("cart")
    public CartDTO cart(Principal principal) {
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

    @GetMapping("/products")
    @ResponseBody
    public CartDTO getCart(Principal principal) {
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

    @PostMapping("/remove")
    @ResponseBody
    public void removeFromCart(@RequestBody RemoveFromCartDTO removeFromCartDTO, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName());
        Optional<Product> addableProduct = productRepository.findById(removeFromCartDTO.getProductId());
        if (addableProduct.isPresent()) {
            currentUser.removeProduct(addableProduct.get());
            userRepository.save(currentUser);
        }
    }
}
