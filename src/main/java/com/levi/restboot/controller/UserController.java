package com.levi.restboot.controller;

import com.levi.restboot.entity.User;
import com.levi.restboot.service.UserService;
import com.levi.restboot.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(path = "/{name}", method = RequestMethod.GET)
    public @ResponseBody User getUserByName(@PathVariable String name){
        return userService.getUserByName(name);
    }

}
