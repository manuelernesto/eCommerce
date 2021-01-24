package com.example.ecommerce.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.persistence.Cart;
import com.example.ecommerce.model.persistence.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
