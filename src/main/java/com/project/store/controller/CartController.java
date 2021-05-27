package com.project.store.controller;

import com.project.store.domain.Product;
import com.project.store.domain.User;
import com.project.store.model.AddToCart;
import com.project.store.model.Cart;
import com.project.store.model.RemoveFromCart;
import com.project.store.repository.ProductRepository;
import com.project.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @GetMapping
    public String getCartPage() {
        return "cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public void addToCart(@RequestBody AddToCart addToCart, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName());
        Optional<Product> addableProduct = productRepository.findById(addToCart.getProductId());
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
    public Cart cart(Principal principal) {
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

    @GetMapping("/products")
    @ResponseBody
    public Cart getCart(Principal principal) {
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

    @PostMapping("/remove")
    @ResponseBody
    public void removeFromCart(@RequestBody RemoveFromCart removeFromCart, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName());
        Optional<Product> addableProduct = productRepository.findById(removeFromCart.getProductId());
        if (addableProduct.isPresent()) {
            currentUser.removeProduct(addableProduct.get());
            userRepository.save(currentUser);
        }
    }

}
