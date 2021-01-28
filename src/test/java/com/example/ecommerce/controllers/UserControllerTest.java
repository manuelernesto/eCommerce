package com.example.ecommerce.controllers;

import com.example.ecommerce.model.persistence.repositories.UserRepository;
import com.example.ecommerce.model.requests.CreateUserRequest;
import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private JacksonTester<CreateUserRequest> json;

    private final String jwt = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW51ZWxlcm5lc3QwIiwiZXhwIjoxNjEyMTkwODUxfQ.64R8XMKzCxH4oeqV_vGHhkRfdM0ZrW9koC2sXD_JnDBYe9UtxP4YyV-L3L25ZftYUuEiYEPaZog3_9k2FuqTQ";
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final String username = "manuelernesto";
    private final String password = "manuelernesto";
    private final String wrongPassword = "manuelernest0";
    private final String lessPassword = "manuel";

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    public void findByIdTest() throws Exception {
        CreateUserRequest createUserRequest = getUserRequest();
        MvcResult postResult = mockMvc.perform(post(new URI("/api/user/create"))
                .content(json.write(createUserRequest).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        Integer id = JsonPath.read(postResult.getResponse().getContentAsString(), "$.id");
        String uri = "/api/user/id/" + id;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(new URI(uri))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    public void findByUserNameTest() throws Exception {
        CreateUserRequest createUserRequest = getUserRequest();
        mockMvc.perform(post(new URI("/api/user/create"))
                .content(json.write(createUserRequest).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(new URI("/api/user/" + username))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    public void createUserTest() throws Exception {
        CreateUserRequest createUserRequest = getUserRequest();
        mockMvc.perform(post(new URI("/api/user/create"))
                .content(json.write(createUserRequest).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

//    @Test
//    public void loginTest() throws Exception {
//        CreateUserRequest user = getUserRequest();
//        createUser();
//        MvcResult response = mockMvc.perform(
//                post("/login")
//                        .content(json.write(user).getJson())
//                        .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk())
//                .andReturn();
//        httpHeaders.set(HttpHeaders.AUTHORIZATION, response.getResponse().getHeader("Authorization"));
//    }

    @Test
    public void passwordDoesNotMatchTest() throws Exception {
        CreateUserRequest createUserRequest = getUserRequest();
        createUserRequest.setConfirmPassword(wrongPassword);
        mockMvc.perform(post(new URI("/api/user/create"))
                .content(json.write(createUserRequest).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void passwordLessThanSevenCharsTest() throws Exception {
        CreateUserRequest createUserRequest = getUserRequest();
        createUserRequest.setConfirmPassword(lessPassword);
        mockMvc.perform(post(new URI("/api/user/create"))
                .content(json.write(createUserRequest).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    /*Private Methods*/
    private CreateUserRequest getUserRequest() {
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername(username);
        user.setPassword(password);
        user.setConfirmPassword(password);
        return user;
    }

    private void createUser() throws Exception {
        CreateUserRequest createUserRequest = getUserRequest();
        mockMvc.perform(post(new URI("/api/user/create"))
                .content(json.write(createUserRequest).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
