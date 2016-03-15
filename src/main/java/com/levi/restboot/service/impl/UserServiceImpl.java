package com.levi.restboot.service.impl;

import com.levi.restboot.entity.User;
import com.levi.restboot.repository.UserRepository;
import com.levi.restboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByName(String name) {
        return userRepository.findUserByName(name);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
