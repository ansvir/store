package com.project.store.controller;

import com.project.store.domain.Product;
import com.project.store.domain.User;
import com.project.store.dto.CartDTO;
import com.project.store.repository.ProductRepository;
import com.project.store.repository.TagRepository;
import com.project.store.repository.UserRepository;
import com.project.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public String getProductsPage(Model model) {
        populateProducts(model);
        return "products";
    }

    @GetMapping("/get/all")
    @ResponseBody
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/add")
    @ResponseBody
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PutMapping("/change/{id}")
    @ResponseBody
    public Product changeProduct(@RequestBody Product product, @PathVariable Long id) {
        return productService.changeProduct(product, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        productService.deleteById(id);
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
