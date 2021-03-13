package com.example.ecommerce.controllers;

import com.example.ecommerce.TestUtils;
import com.example.ecommerce.model.persistence.Cart;
import com.example.ecommerce.model.persistence.Item;
import com.example.ecommerce.model.persistence.User;
import com.example.ecommerce.model.persistence.UserOrder;
import com.example.ecommerce.model.persistence.repositories.OrderRepository;
import com.example.ecommerce.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private final OrderRepository orderRepo = mock(OrderRepository.class);
    private final UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController(null, null);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "userRepository", userRepo);

        Item item = new Item();
        item.setId(1L);
        item.setName("Java Web Book");
        BigDecimal price = BigDecimal.valueOf(7.19);
        item.setPrice(price);
        item.setDescription("Time to refresh to knowledge");
        List<Item> items = new ArrayList<>();
        items.add(item);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("manuelernesto");
        user.setPassword("manuelernest0Password");
        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(2.99);
        cart.setTotal(total);
        user.setCart(cart);
        when(userRepo.findByUsername("manuelernesto")).thenReturn(user);
        when(userRepo.findByUsername("ernesto")).thenReturn(null);

    }

    @Test
    public void submit_order_Test() {
        ResponseEntity<UserOrder> response = orderController.submit("manuelernesto");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
    }

    @Test
    public void submit_order_user_not_found_Test() {
        ResponseEntity<UserOrder> response = orderController.submit("ernesto");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_orders_for_user_Test() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("manuelernesto");
        assertNotNull(ordersForUser);
        assertEquals(200, ordersForUser.getStatusCodeValue());
        List<UserOrder> orders = ordersForUser.getBody();
        assertNotNull(orders);

    }

    @Test
    public void get_orders_for_user_not_found_Test() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("ernesto");
        assertNotNull(ordersForUser);
        assertEquals(404, ordersForUser.getStatusCodeValue());

    }
}
