package com.example.ecommerce.controllers;

import com.example.ecommerce.TestUtils;
import com.example.ecommerce.model.persistence.Cart;
import com.example.ecommerce.model.persistence.User;
import com.example.ecommerce.model.persistence.repositories.CartRepository;
import com.example.ecommerce.model.persistence.repositories.UserRepository;
import com.example.ecommerce.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerTest {

    private UserController userController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setUp() {
        userController = new UserController(null, null, null);
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        User user = new User();
        Cart cart = new Cart();

        user.setId(0);
        user.setUsername("manuelernesto");
        user.setPassword("manuelernest0Password");
        user.setCart(cart);

        when(userRepository.findByUsername("manuelernesto")).thenReturn(user);
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findByUsername("ernesto")).thenReturn(null);

    }


    @Test
    public void create_user_Test() {
        when(encoder.encode("manuelernest0Password")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("manuelernesto");
        r.setPassword("manuelernest0Password");
        r.setConfirmPassword("manuelernest0Password");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("manuelernesto", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

    }


    @Test
    public void create_user_password_too_short_Test() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("manuelernesto");
        r.setPassword("manuel");
        r.setConfirmPassword("manuel");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }


    @Test
    public void create_user_password_confirm_mismatch_Test() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("manuelernesto");
        r.setPassword("manuelernest0Password");
        r.setConfirmPassword("manuelernestoPassword");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_name_Test() {
        final ResponseEntity<User> response = userController.findByUserName("manuelernesto");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals("manuelernesto", u.getUsername());
    }

    @Test
    public void find_user_by_name_doesnt_exist_Test() {
        final ResponseEntity<User> response = userController.findByUserName("manuel");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_id_Test() {
        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        ;
    }


    @Test
    public void find_user_by_id_doesnt_exist_Test() {
        final ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
