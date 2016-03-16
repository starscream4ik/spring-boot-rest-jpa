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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @Mock
    private static UserService userService;
    @InjectMocks
    private static UserController userController;
    List<User> users = new ArrayList<>();
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        users.add(new User(1L, "Andrew", "Petrovych", "+380632528"));
        users.add(new User(2L, "Sasha", "Petrovych", "+380632528"));
        users.add(new User(3L, "Petya", "Petrovych", "+380632528"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        //given
        when(userService.getAllUsers()).thenReturn(users);

        //when
        ResultActions result = mockMvc.perform(get("/users"));

        //then
        verify(userService, times(1)).getAllUsers();
        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        List usersFromResponse = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsByteArray(), List.class);
        Assert.assertEquals(usersFromResponse.size(), users.size());
    }

    @Test
    public void testGettingUserById() throws Exception {
        //given
        long id = 1;
        long idOfNotExistingUser = 5;
        when(userService.getUserById(id)).thenReturn(users.stream().filter(user -> user.getId() == id).findFirst().get());
        when(userService.getUserById(idOfNotExistingUser)).thenReturn(null);

        //when
        ResultActions resultPositive = mockMvc.perform(get("/users/{id}", id));
        ResultActions resultNegative = mockMvc.perform(get("/users/{id}", idOfNotExistingUser));

        //then
        verify(userService, times(1)).getUserById(id);
        resultPositive.andExpect(status().isOk());
        resultPositive.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        User userFromResponse = new ObjectMapper().readValue(resultPositive.andReturn().getResponse().getContentAsByteArray(), User.class);
        Assert.assertEquals(id, userFromResponse.getId());

        verify(userService, times(1)).getUserById(idOfNotExistingUser);
        resultPositive.andExpect(status().isOk());
        resultPositive.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        int contentLength = resultNegative.andReturn().getResponse().getContentLength();
        Assert.assertEquals(0, contentLength);


    }
}