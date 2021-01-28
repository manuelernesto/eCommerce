package com.example.ecommerce.controllers;

import com.example.ecommerce.model.persistence.User;
import com.example.ecommerce.model.persistence.repositories.UserRepository;
import com.example.ecommerce.model.requests.CreateUserRequest;
import com.example.ecommerce.model.requests.ModifyCartRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CartControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    UserRepository userRepository;

    private final HttpHeaders httpHeaders = new HttpHeaders();

    @Before
    public void setup() throws Exception {
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("dexter");
        user.setPassword("password");

        User result = userRepository.findByUsername(user.getUsername());
        String endpoint = Objects.isNull(result) ? "api/user/create" : "login";
        MvcResult response = mvc.perform(
                post("/".concat(endpoint))
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, response.getResponse().getHeader("Authorization"));
    }

    @Test
    public void addToCart() throws Exception {
        ModifyCartRequest body = new ModifyCartRequest();
        body.setItemId(1);
        body.setQuantity(2);

        mvc.perform(
                post("/api/cart/addToCart")
                        .headers(httpHeaders)
                        .content(mapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void removeFromCart() throws Exception {
        ModifyCartRequest body = new ModifyCartRequest();
        body.setItemId(1);
        body.setQuantity(1);
        mvc.perform(
                post("/api/cart/removeFromCart")
                        .headers(httpHeaders)
                        .content(mapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

}
