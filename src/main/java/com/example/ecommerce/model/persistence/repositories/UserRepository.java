package com.example.ecommerce.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.persistence.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
