package com.example.ecommerce.controllers;

import com.example.ecommerce.model.persistence.Cart;
import com.example.ecommerce.model.persistence.User;
import com.example.ecommerce.model.persistence.repositories.CartRepository;
import com.example.ecommerce.model.persistence.repositories.UserRepository;
import com.example.ecommerce.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository, CartRepository cartRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        logger.info("Get user by Id:{}", id);
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        logger.info("Get user by username:{}", username);
        User user = userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        if (createUserRequest.getPassword().length() < 7 ||
                !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().build();
        }
        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);
        logger.info("Create new user with username :{}", user.getUsername());
        return ResponseEntity.ok(user);
    }

}
