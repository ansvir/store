# Store application
Application written on Java that allows to perform simple actions of view and purchasing products, manage them using administrator console

## Stack:

1. Spring Boot 2.5.0
2. Java 14
3. Thymeleaf 2.5.0
4. Apache Tomcat 9.0.46
5. H2 database

## Features:


**For all**:
1. Login as a user, administrator
2. Register as a user. Admins are created straightly using h2-console or command line
3. Searching products by description and filtering by tags

**For users**:
1. Add and delete products from cart for users
2. Purchase added to cart products, simply clicking 'Purchase' button and getting notification by email

**For administrators**:
1. CRUD over products: view, create, update and delete
2. Force update product if product was appeared in the users carts, notificating them

Default email for all users (administrators): svirepa.anton@gmail.com. It can be changed in com.project.store.database.DatabaseLoader.java class

## How to run an application:
1. git clone https://github.com/ansvir/store
2. run in the root folder: <code>mvn spring-boot:run</code>
