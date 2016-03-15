package com.levi.restboot.controller;

import com.levi.restboot.entity.User;
import com.levi.restboot.service.UserService;
import com.levi.restboot.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public @ResponseBody User updateUser(@PathVariable Long id, @RequestBody User user){
        if(userService.getUserById(id) == null){
            // TODO return proper answer
            return null;
        }
        user.setId(id);
        return userService.updateUser(user);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return "redirect:/users";
    }


}
