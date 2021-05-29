package com.project.store.controller;

import com.project.store.domain.Order;
import com.project.store.domain.Product;
import com.project.store.domain.User;
import com.project.store.model.CartDTO;
import com.project.store.repository.OrderRepository;
import com.project.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/commit")
    public String commitOrder(Principal principal) {
        Order order = new Order();
        User currentUser = userRepository.findByUsername(principal.getName());
        Set<Product> orderProducts = new LinkedHashSet<>(currentUser.getProducts());
        order.setProducts(orderProducts);
        orderRepository.save(order);
        sendMessage(order, currentUser);
        currentUser.retainProducts();
        userRepository.save(currentUser);
        return "redirect:/";
    }

    private void sendMessage(Order order, User toUser) {
        Set<Product> purchasedProducts = toUser.getProducts();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("<store.auto.sender@gmail.com>");
        message.setTo(toUser.getEmail());

        StringBuilder purchasedProductsMessage = new StringBuilder();
        message.setSubject("Store: order #" + order.getId() + " committed.");

        int i = 1;
        for (Product product : purchasedProducts) {
            purchasedProductsMessage.append(i);
            purchasedProductsMessage.append(". ");
            purchasedProductsMessage.append(product.getName());
            purchasedProductsMessage.append("\n");
            i++;
        }

        String info = "Do NOT respond to this message, it was automatically sent!";

        message.setText("Thank you for purchasing in out store! Ordered products will be delivered soon:\n" + purchasedProductsMessage.toString() + "\n" + info);
        javaMailSender.send(message);
    }
}
