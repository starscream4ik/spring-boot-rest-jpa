package com.levi.restboot.service;

import com.levi.restboot.entity.User;

import java.util.List;

/**
 * Created by a.petrovych on 15-Mar-16.
 */
public interface UserService {

    User getUserByName(String name);
    List<User> getAllUsers();
}
