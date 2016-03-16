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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @Mock
    private static UserService userService;
    @InjectMocks
    private static UserController userController;
    List<User> users = new ArrayList<>();
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MediaType contentType = MediaType.APPLICATION_JSON_UTF8;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        this.mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

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
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON));
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
        resultPositive.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        User userFromResponse = new ObjectMapper().readValue(resultPositive.andReturn().getResponse().getContentAsByteArray(), User.class);
        Assert.assertEquals(id, userFromResponse.getId());

        verify(userService, times(1)).getUserById(idOfNotExistingUser);
        resultNegative.andExpect(status().isOk());
        int contentLength = resultNegative.andReturn().getResponse().getContentLength();
        Assert.assertEquals(0, contentLength);
    }


    @Test
    public void testAddingUser() throws Exception {
        //given
        User userToCreate = new User(5L, "Valentyn", "Strykalo", "+3685426");
        when(userService.createUser(userToCreate)).thenReturn(userToCreate);
        String userJson = json(userToCreate);

        //when
        ResultActions result = mockMvc.perform(post("/users").content(userJson).contentType(contentType));

        //then
        verify(userService, times(1)).createUser(any(User.class));
        result.andExpect(status().isCreated());
    }

    @Test
    public void testDeleteUser() throws Exception {
        //given
        long id = 1;
        doNothing().when(userService).deleteUser(id);

        //when
        ResultActions result = mockMvc.perform(delete("/users/{id}", id).contentType(contentType));

        //then
        verify(userService, times(1)).deleteUser(id);
        result.andExpect(status().isOk());
        result.andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateUser() throws Exception {
        //given
        long idOfExistingUser = 1;
        User existingUser = users.stream().filter(user -> user.getId() == idOfExistingUser).findFirst().get();

        long idOfNotExistingUser = 5;
        User notExistingUser = new User(idOfNotExistingUser, "Not", "Exists", "+3555");

        when(userService.getUserById(idOfExistingUser)).thenReturn(existingUser);
        when(userService.getUserById(idOfNotExistingUser)).thenReturn(null);

        when(userService.updateUser(existingUser)).thenReturn(existingUser);

        //when
        ResultActions resultPositive = mockMvc.perform(put("/users/{id}", idOfExistingUser).content(json(existingUser)).contentType(contentType));
        ResultActions resultNegative = mockMvc.perform(put("/users/{id}", idOfNotExistingUser).content(json(notExistingUser)).contentType(contentType));

        //then
        verify(userService, times(1)).updateUser(any(User.class));
        resultPositive.andExpect(status().isOk());

        verify(userService, times(1)).getUserById(idOfNotExistingUser);
        resultNegative.andExpect(status().isNotModified());
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}