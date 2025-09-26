package com.fit.demo.Users.controller;

import org.springframework.web.bind.annotation.*;

import com.fit.demo.Users.entidades.User;
import com.fit.demo.Users.entidades.UserResponse;
import com.fit.demo.Users.repositry.UserRepository;
import com.fit.demo.Users.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repository;
    private final UserService userService;

    public UserController(UserRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return repository.findAll();
    }
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return repository.save(user);
    }
    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User userDetails) {
       return userService.updateUser(id, userDetails);
         
    }

    @PatchMapping("/{id}")
    public User patchUser(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        return userService.patchUser(id, updates);
    }
}