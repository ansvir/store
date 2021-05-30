package com.project.store.controller;

import com.project.store.domain.*;
import com.project.store.repository.ForceUpdateRepository;
import com.project.store.repository.ProductRepository;
import com.project.store.repository.TagRepository;
import com.project.store.repository.UserRepository;
import com.project.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ForceUpdateRepository forceUpdateRepository;

    @GetMapping
    public String adminConsole(Model model) {
        populateAdminConsole(model);
        return "admin";
    }

    @PostMapping("/command")
    public String processProductAction(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("command") String command, Model model, Principal principal) {
        switch (command) {
            case "change_product": {
                List<User> usersProductInCart = findUsersProductInCart(product);
                if (!usersProductInCart.isEmpty()) {
                    User currentAdmin = userRepository.findByUsername(principal.getName());
                    if (sendNotificationProductInCart(usersProductInCart, currentAdmin, product)) {
                        ForceUpdate forceUpdate = new ForceUpdate();
                        System.out.println(product.toString());
                        forceUpdate.setProduct(product);
                        forceUpdateRepository.save(forceUpdate);
                        model.addAttribute("forceUpdateProduct", forceUpdate);
                    }
                    break;
                }
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
            case "force_update": {
                List<ForceUpdate> forceUpdateList = (List<ForceUpdate>) forceUpdateRepository.findAll();
                Product productToForceUpdate = null;
                for (ForceUpdate update : forceUpdateList) {
                    if (update.getProduct().getId().equals(product.getId())) {
                        productToForceUpdate = update.getProduct();
                        System.out.println(productToForceUpdate.toString());
                        break;
                    }
                }
                productService.changeProduct(productToForceUpdate, productToForceUpdate.getId());
                sendNotificationProductForceUpdated(product, productToForceUpdate);
                forceUpdateRepository.deleteById(product.getId());
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
        Set<Product> productsToForceUpdate = new LinkedHashSet<>();
        forceUpdateRepository.findAll().forEach(update -> {
            productsToForceUpdate.add(update.getProduct());
        });
        model.addAttribute("forceUpdatesProducts", productsToForceUpdate);
    }

    private boolean sendNotificationProductInCart(List<User> usersProductInCart, User toAdmin, Product productInCart) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("<store.auto.sender@gmail.com>");
        message.setTo(toAdmin.getEmail());

        message.setSubject("Product with the id #" + productInCart.getId() + " already in cart");

        StringBuilder messageBody = new StringBuilder();
        messageBody.append("The next users are already have product [id=");
        messageBody.append(productInCart.getId());
        messageBody.append(", name=");
        messageBody.append(productInCart.getName());
        messageBody.append("] in cart:\n\n");

        int i = 1;
        for (User user : usersProductInCart) {
            messageBody.append(i);
            messageBody.append(". ");
            messageBody.append(user.getUsername());
            messageBody.append("[id=");
            messageBody.append(user.getId());
            messageBody.append("];\n");
            i++;
        }

        messageBody.append("Do NOT respond to this message, it was automatically sent!\n");
        message.setText(messageBody.toString());
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void sendNotificationProductForceUpdated(Product product, Product updated) {
        List<User> usersProductInCart = findUsersProductInCart(product);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("<store.auto.sender@gmail.com>");
        List<String> emails = usersProductInCart.stream().map(User::getEmail).collect(Collectors.toList());
        String[] emailsArray = new String[emails.size()];
        for (int i = 0; i < emails.size(); i++) {
            emailsArray[i] = emails.get(i);
        }
        message.setTo(emailsArray);

        message.setSubject("Product '" + product.getName() + "' was changed");

        StringBuilder messageBody = new StringBuilder();
        messageBody.append("The next product was changed and changes will be appeared in your cart:\n");
        messageBody.append("Name: ");
        messageBody.append(product.getName());
        messageBody.append("\n");
        messageBody.append("Description: ");
        messageBody.append(product.getDescription());
        messageBody.append("\n");
        messageBody.append("Tags: ");
        product.getTags().forEach(tag -> {
            messageBody.append(tag.getName());
            messageBody.append(", ");
        });
        messageBody.substring(0, messageBody.lastIndexOf(",") - 1);
        messageBody.append(".\n");
        messageBody.append("Was changed to:\n");
        messageBody.append("Name: ");
        messageBody.append(updated.getName());
        messageBody.append("\n");
        messageBody.append("Description: ");
        messageBody.append(updated.getDescription());
        messageBody.append("\n");
        messageBody.append("Tags: ");
        updated.getTags().forEach(tag -> {
            messageBody.append(tag.getName());
            messageBody.append(", ");
        });
        messageBody.substring(0, messageBody.lastIndexOf(",") - 1);
        messageBody.append(".\n");
        messageBody.append("Do NOT respond to this message, it was automatically sent!\n");
        message.setText(messageBody.toString());
        javaMailSender.send(message);
    }

    private List<User> findUsersProductInCart(Product product) {
        List<User> users = (List<User>) userRepository.findAll();
        List<User> usersProductInCart = new ArrayList<>();
        users.forEach(user -> {
            Set<Product> userProducts = user.getProducts();
            userProducts.forEach(userProduct -> {
                if (product.getId().equals(userProduct.getId())) {
                    usersProductInCart.add(user);
                }
            });
        });

        return usersProductInCart;
    }
}
