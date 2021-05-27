package com.project.store.database;

import com.project.store.domain.*;
import com.project.store.repository.ProductRepository;
import com.project.store.repository.RoleRepository;
import com.project.store.repository.TagRepository;
import com.project.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseLoader implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Role userRole = new Role();
        userRole.setName(RoleEnum.ROLE_USER.name());
        roleRepository.save(userRole);
        Role adminRole = new Role();
        adminRole.setName(RoleEnum.ROLE_ADMINISTRATOR.name());
        roleRepository.save(adminRole);

        User user = new User("namadoni", passwordEncoder.encode("pass1"), "svirepa.anton@gmail.com");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleRepository.findByName(RoleEnum.ROLE_USER.name()));
        user.setRoles(userRoles);
        userRepository.save(user);
        User user2 = new User("svirepa.a", passwordEncoder.encode("password"), "svirepa.anton@gmail.com");
        Set<Role> user2Roles = new HashSet<>();
        user2Roles.add(roleRepository.findByName(RoleEnum.ROLE_USER.name()));
        user2.setRoles(user2Roles);
        userRepository.save(user2);
        User admin = new User("admin", passwordEncoder.encode("admin"), "svirepa.anton@gmail.com");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(roleRepository.findByName(RoleEnum.ROLE_ADMINISTRATOR.name()));
        admin.setRoles(adminRoles);
        userRepository.save(admin);

        Tag tag = new Tag();
        tag.setName("Technics");
        Tag tag2 = new Tag();
        tag2.setName("Clothes");
        Tag tag3 = new Tag();
        tag3.setName("Tag3");
        Tag tag4 = new Tag();
        tag4.setName("Electronic");
        Tag tag5 = new Tag();
        tag5.setName("Entertainment");
        Tag tag6 = new Tag();
        tag6.setName("Agriculture");
        Tag tag7 = new Tag();
        tag7.setName("Equipment");

        tagRepository.save(tag);
        tagRepository.save(tag2);
        tagRepository.save(tag3);
        tagRepository.save(tag4);
        tagRepository.save(tag5);
        tagRepository.save(tag6);
        tagRepository.save(tag7);

        Product product = new Product();
        product.setName("Radio");
        product.setDescription("Simple radio that allows listen to broadcasting programs");
        Set<Tag> productTags = new HashSet<>();
        productTags.add(tagRepository.findById(1L).get());
        productTags.add(tagRepository.findById(4L).get());
        productTags.add(tagRepository.findById(5L).get());
        product.setTags(productTags);
        productRepository.save(product);
        Product product2 = new Product();
        product2.setName("TV");
        product2.setDescription("Silver color TV that have 50\" diagonal");
        Set<Tag> product2Tags = new HashSet<>();
        product2Tags.add(tagRepository.findById(1L).get());
        product2Tags.add(tagRepository.findById(4L).get());
        product2Tags.add(tagRepository.findById(5L).get());
        product2.setTags(product2Tags);
        productRepository.save(product2);
        Product product3 = new Product();
        product3.setName("Monitor");
        product3.setDescription("Computer monitor with 20\" diagonal");
        Set<Tag> product3Tags = new HashSet<>();
        product3Tags.add(tagRepository.findById(1L).get());
        product3Tags.add(tagRepository.findById(4L).get());
        product3Tags.add(tagRepository.findById(5L).get());
        product3.setTags(product3Tags);
        productRepository.save(product3);
        Product product4 = new Product();
        product4.setName("Painting");
        product4.setDescription("Painting with the picture of dog");
        Set<Tag> product4Tags = new HashSet<>();
        product4Tags.add(tagRepository.findById(5L).get());
        product4.setTags(product4Tags);
        productRepository.save(product4);
        Product product5 = new Product();
        product5.setName("Plant");
        product5.setDescription("Plant with the red flower");
        Set<Tag> product5Tags = new HashSet<>();
        product5Tags.add(tagRepository.findById(6L).get());
        product5.setTags(product5Tags);
        productRepository.save(product5);
        Product product6 = new Product();
        product6.setName("The network wire");
        product6.setDescription("Network wire with the RJ45 output");
        Set<Tag> product6Tags = new HashSet<>();
        product6Tags.add(tagRepository.findById(4L).get());
        product6Tags.add(tagRepository.findById(7L).get());
        product6.setTags(product6Tags);
        productRepository.save(product6);
        Product product7 = new Product();
        product7.setName("Pants");
        product7.setDescription("Pants of the gray color");
        Set<Tag> product7Tags = new HashSet<>();
        product7Tags.add(tagRepository.findById(2L).get());
        product7Tags.add(tagRepository.findById(7L).get());
        product7.setTags(product7Tags);
        productRepository.save(product7);
        Product product8 = new Product();
        product8.setName("Hat");
        product8.setDescription("Cone style red hat");
        Set<Tag> product8Tags = new HashSet<>();
        product8Tags.add(tagRepository.findById(2L).get());
        product8Tags.add(tagRepository.findById(7L).get());
        product8.setTags(product8Tags);
        productRepository.save(product8);

        Set<Product> userProducts = new HashSet<>();
        userProducts.add(product);
        userProducts.add(product2);
        userProducts.add(product3);
        user.setProducts(userProducts);
        userRepository.save(user);
    }
}
