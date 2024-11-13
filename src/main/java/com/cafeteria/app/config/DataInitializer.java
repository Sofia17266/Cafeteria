package com.cafeteria.app.config;

import com.cafeteria.app.model.*;
import com.cafeteria.app.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            SubcategoryRepository subcategoryRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
        // Roles
        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(RoleName.ADMIN)));
        Role customerRole = roleRepository.findByName(RoleName.CUSTOMER)
                .orElseGet(() -> roleRepository.save(new Role(RoleName.CUSTOMER)));
        Role guestRole = roleRepository.findByName(RoleName.GUEST)
                .orElseGet(() -> roleRepository.save(new Role(RoleName.GUEST)));
        
        
        // Usuarios
            userRepository.findByEmail("admin@cafeteria.com").orElseGet(() -> {
                User adminUser = new User(
                        "Admin",
                        "1234567890",
                        "admin@cafeteria.com",
                        passwordEncoder.encode("admin123"),
                        adminRole
                );
                return userRepository.save(adminUser);
            });

            userRepository.findByEmail("cliente@cafeteria.com").orElseGet(() -> {
                User customerUser = new User(
                        "Cliente",
                        "1122334455",
                        "cliente@cafeteria.com",
                        passwordEncoder.encode("cliente123"),
                        customerRole
                );
                return userRepository.save(customerUser);
            });
            
            // Categorías
            Category bakery = createCategoryIfNotExists("Pastelería", categoryRepository);
            Category desserts = createCategoryIfNotExists("Postres", categoryRepository);
            Category savoryBakery = createCategoryIfNotExists("Repostería Salada", categoryRepository);
            Category breakfast = createCategoryIfNotExists("Desayunos", categoryRepository);
            Category salads = createCategoryIfNotExists("Ensaladas", categoryRepository);
            Category sharing = createCategoryIfNotExists("Para Compartir", categoryRepository);
            Category coldBeverages = createCategoryIfNotExists("Bebidas Frías", categoryRepository);
            Category hotBeverages = createCategoryIfNotExists("Bebidas Calientes", categoryRepository);
            Category smoothies = createCategoryIfNotExists("Smoothies", categoryRepository);

            // Subcategorías productos
            Subcategory bowl = createSubcategoryIfNotExists("Bowl", breakfast, subcategoryRepository);
            Subcategory sweet = createSubcategoryIfNotExists("Dulce", breakfast, subcategoryRepository);
            Subcategory savory = createSubcategoryIfNotExists("Salado", breakfast, subcategoryRepository);
            Subcategory brunch = createSubcategoryIfNotExists("Brunch", breakfast, subcategoryRepository);

            // Añadir categorías/subcategorías a los productos
            createProductIfNotExists("Pastel de Chocolate", 
                "Delicioso pastel de chocolate con un relleno de dulce de leche y Nutella, cubierto con chispas de chocolate y otra capa de Nutella.", 
                12000, "pastel_chocolate.jpg", bakery, null, productRepository);
            createProductIfNotExists("Pastel de Red Velvet", 
                "Pastel de red velvet con relleno de leche condensada y una cobertura de coco.", 
                12000, "pastel_red_velvet.jpg", bakery, null, productRepository);
            createProductIfNotExists("Pastel Frío de Frutas y Vainilla", 
                "Pastel frío de frutas y vainilla con crema pastelera y esponja de vainilla.", 
                12000, "pastel_frutas_vainilla.jpg", bakery, null, productRepository);

            // Postres
            createProductIfNotExists("Cheesecake de Frutos Rojos", 
                "Cheesecake con una variedad de frutos rojos.", 
                2500, "cheesecake_frutos_rojos.jpg", desserts, null, productRepository);
            createProductIfNotExists("Cheesecake de Maracuyá", 
                "Cheesecake con sabor a maracuyá.", 
                2500, "cheesecake_maracuya.jpg", desserts, null, productRepository);
            createProductIfNotExists("Cheesecake de Zanahoria", 
                "Cheesecake con un toque de zanahoria.", 
                2500, "cheesecake_zanahoria.jpg", desserts, null, productRepository);
            createProductIfNotExists("Cheesecake de Oreo", 
                "Cheesecake con sabor a Oreo.", 
                2500, "cheesecake_oreo.jpg", desserts, null, productRepository);

            // Desayunos - Bowl
            createProductIfNotExists("Bowl de Fresa", 
                "Bowl con base de fresa y toppings de frutas frescas como frambuesa, almendra y mango.", 
                3500, "bowl_fresa.jpg", breakfast, bowl, productRepository);
            createProductIfNotExists("Bowl de Açaí", 
                "Bowl de açaí con toppings de kiwi, arándano, coco y mango.", 
                3500, "bowl_acai.jpg", breakfast, bowl, productRepository);

            // Desayunos - Dulce
            createProductIfNotExists("Tostadas Francesas con Frutas", 
                "Deliciosas tostadas francesas con frambuesas y miel de maple.", 
                3500, "tostadas_francesas_frutas.jpg", breakfast, sweet, productRepository);
            createProductIfNotExists("Tostadas Francesas con Nutella", 
                "Tostadas francesas rellenas de Nutella.", 
                3500, "tostadas_francesas_nutella.jpg", breakfast, sweet, productRepository);

            // Desayunos - Salado
            createProductIfNotExists("Croissant con Aguacate y Pavo", 
                "Croissant relleno de aguacate, jamón de pavo y queso fresco.", 
                4500, "croissant_aguacate_pavo.jpg", breakfast, savory, productRepository);

            // Desayunos - Brunch
            createProductIfNotExists("Brunch de Pinto", 
                "Gallo pinto con queso frito, plátano maduro y aguacate.", 
                5500, "brunch_pinto.jpg", breakfast, brunch, productRepository);

            
            // Para Compartir
            createProductIfNotExists("Papas Fritas", "Orden de papas fritas.", 2500, "papas_fritas.jpg", sharing, null, productRepository);
            createProductIfNotExists("Papas a la Mexicana", "Orden de papas a la mexicana.", 3000, "papas_mexicana.jpg", sharing, null, productRepository);
            createProductIfNotExists("Papas Planas", "Orden de papas planas.", 2500, "papas_planas.jpg", sharing, null, productRepository);
            createProductIfNotExists("Camote Frito", "Orden de camote frito.", 2500, "camote_frito.jpg", sharing, null, productRepository);

            // Ensaladas
            createProductIfNotExists("Ensalada de la Casa", "Ensalada de la casa con espinaca, tomate, zanahoria, queso parmesano, y bacon.", 4500, "ensalada_casa.jpg", salads, null, productRepository);
            createProductIfNotExists("Ensalada de Quinoa", "Ensalada de quinoa con brócoli, chile asado, zucchini, zanahoria, tomate cherry, y pepino.", 4500, "ensalada_quinoa.jpg", salads, null, productRepository);
            createProductIfNotExists("Ensalada Fresca", "Ensalada fresca con lechuga, tomate cherry, zanahoria, mango, y ajonjolí.", 4500, "ensalada_fresca.jpg", salads, null, productRepository);
            createProductIfNotExists("Ensalada con Lechuga y Pepino", "Ensalada con lechuga, pepino, cebolla morada, rábano, y crotones.", 4500, "ensalada_lechuga.jpg", salads, null, productRepository);

            // Bebidas Frías
            createProductIfNotExists("Salted Caramel Coffee", "Café frío con caramelo salado.", 2500, "salted_caramel_coffee.jpg", coldBeverages, null, productRepository);
            createProductIfNotExists("Iced Brown Sugar", "Café frío con azúcar moreno.", 3500, "iced_brown_sugar.jpg", coldBeverages, null, productRepository);
            createProductIfNotExists("Mocaccino", "Mocaccino frío.", 2500, "mocaccino.jpg", coldBeverages, null, productRepository);
            createProductIfNotExists("Jelly Coffee", "Café frío con gelatina.", 2500, "jelly_coffee.jpg", coldBeverages, null, productRepository);

            // Bebidas Calientes
            createProductIfNotExists("Café Americano", "Café Americano caliente.", 2500, "cafe_americano.jpg", hotBeverages, null, productRepository);
            createProductIfNotExists("Pumpkin Spice Latte", "Latte caliente con especias de calabaza.", 3500, "pumpkin_spice_latte.jpg", hotBeverages, null, productRepository);
            createProductIfNotExists("Chocolate Caliente", "Chocolate caliente.", 2500, "chocolate_caliente.jpg", hotBeverages, null, productRepository);
            createProductIfNotExists("Mocha", "Café mocha caliente.", 2500, "mocha.jpg", hotBeverages, null, productRepository);

            // Smoothies
            createProductIfNotExists("Batido Verde", "Smoothie de vegetales verdes.", 2500, "batido_verde.jpg", smoothies, null, productRepository);
            createProductIfNotExists("Smoothie de Sandía", "Smoothie de sandía.", 2500, "smoothie_sandia.jpg", smoothies, null, productRepository);
            createProductIfNotExists("Smoothie de Banano y Mantequilla de Maní", "Smoothie de banano con mantequilla de maní.", 2500, "smoothie_banano_mantequilla.jpg", smoothies, null, productRepository);
            createProductIfNotExists("Smoothie de Fresa y Mora", "Smoothie de fresa y mora.", 2500, "smoothie_fresa_mora.jpg", smoothies, null, productRepository);

            // Initialize Orders and OrderItems
            User customerUser = userRepository.findByEmail("cliente@cafeteria.com").orElseThrow(() -> new RuntimeException("Customer user not found"));
            createOrderWithItemsIfNotExists(customerUser, orderRepository, orderItemRepository, productRepository);
        };
    }

    // Métodos helper

    private Category createCategoryIfNotExists(String name, CategoryRepository categoryRepository) {
        return categoryRepository.findByName(name).orElseGet(() -> categoryRepository.save(new Category(name)));
    }

    private Subcategory createSubcategoryIfNotExists(String name, Category category, SubcategoryRepository subcategoryRepository) {
        return subcategoryRepository.findByNameAndCategory(name, category)
                .orElseGet(() -> subcategoryRepository.save(new Subcategory(name, category)));
    }

    private Product createProductIfNotExists(String name, String description, double price, String imageUrl, Category category, Subcategory subcategory, ProductRepository productRepository) {
        return productRepository.findByName(name)
                .orElseGet(() -> productRepository.save(new Product(name, description, price, imageUrl, category, subcategory)));
    }

    private void createOrderWithItemsIfNotExists(User customerUser, OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        if (orderRepository.count() > 0) return;

        // Ordenes
        Order order1 = new Order(LocalDateTime.now(), 0.0, customerUser);
        Order order2 = new Order(LocalDateTime.now().minusDays(1), 0.0, customerUser);
        Order order3 = new Order(LocalDateTime.now().minusDays(2), 0.0, customerUser);

        orderRepository.saveAll(List.of(order1, order2, order3));

        OrderItem orderItem1 = createOrderItem(order1, productRepository, "Croissant con Aguacate y Pavo", 2, orderItemRepository);
        OrderItem orderItem2 = createOrderItem(order1, productRepository, "Café Americano", 1, orderItemRepository);
        OrderItem orderItem3 = createOrderItem(order2, productRepository, "Tostadas Francesas con Frutas", 1, orderItemRepository);
        OrderItem orderItem4 = createOrderItem(order3, productRepository, "Jelly Coffee", 2, orderItemRepository);

        order1.setTotalPrice(calculateTotalPrice(List.of(orderItem1, orderItem2)));
        order2.setTotalPrice(calculateTotalPrice(List.of(orderItem3)));
        order3.setTotalPrice(calculateTotalPrice(List.of(orderItem4)));

        orderRepository.saveAll(List.of(order1, order2, order3));
    }

    private OrderItem createOrderItem(Order order, ProductRepository productRepository, String productName, int quantity, OrderItemRepository orderItemRepository) {
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productName));
        OrderItem orderItem = new OrderItem(order, product, quantity);
        return orderItemRepository.save(orderItem);
    }

    private Double calculateTotalPrice(List<OrderItem> items) {
        return items.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
    }
}