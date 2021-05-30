package com.project.store.controller;

import com.project.store.domain.Product;
import com.project.store.domain.Tag;
import com.project.store.repository.ProductRepository;
import com.project.store.repository.TagRepository;
import com.project.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String adminConsole(Model model) {
        populateAdminConsole(model);
        return "admin";
    }

    @PostMapping("/command")
    public String processProductAction(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("command") String command) {
        switch (command) {
            case "change_product": {
                productService.changeProduct(product, product.getId());
                break;
            }
            case "add_product": {
                productService.addProduct(product);
                break;
            }
            case "delete_product": {
                productService.deleteById(product.getId());
                break;
            }
        }
        return "redirect:/admin";
    }

    @ModelAttribute("product")
    public Product product() {
        return new Product();
    }

    private void populateAdminConsole(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());
    }
}
