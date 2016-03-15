package com.levi.restboot.service.impl;

import com.levi.restboot.entity.User;
import com.levi.restboot.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.petrovych on 15-Mar-16.
 */
public class UserServiceImpl implements UserService {

    private static UserServiceImpl INSTANCE;
    private List<User> users;

    private UserServiceImpl() {
        initData();
    }

    private void initData() {
        users = new ArrayList<>();
        users.add(new User(1L, "Andrew", "Petrovych", "4145689"));
        users.add(new User(2L, "Sasha", "Petrovych", "4145689"));
        users.add(new User(3L, "Vasya", "Petrovych", "4145689"));
    }

    public static UserServiceImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserServiceImpl();
        }

        return INSTANCE;
    }

    @Override
    public User getUserByName(String name) {
        // TODO implement properly using Sping DATA;
        for (User userToFind : users) {
            if(userToFind.getName().equals(name)){
                return userToFind;
            }
        }

        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }
}
