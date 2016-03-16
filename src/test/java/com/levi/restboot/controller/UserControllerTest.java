package com.levi.restboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levi.restboot.entity.User;
import com.levi.restboot.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @Mock
    private static UserService userService;
    @InjectMocks
    private static UserController userController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        //given
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "Andrew", "Petrovych", "+380632528"));
        users.add(new User(2L, "Sasha", "Petrovych", "+380632528"));
        users.add(new User(3L, "Petya", "Petrovych", "+380632528"));
        when(userService.getAllUsers()).thenReturn(users);

        //when
        ResultActions result = mockMvc.perform(get("/users"));

        //then
        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        List usersFromResponse = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsByteArray(), List.class);
        Assert.assertEquals(usersFromResponse.size(), users.size());
    }
}