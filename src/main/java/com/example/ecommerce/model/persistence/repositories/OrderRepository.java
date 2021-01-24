package com.example.ecommerce.model.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.persistence.User;
import com.example.ecommerce.model.persistence.UserOrder;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
