package com.fit.demo.Users.controller;

import org.springframework.web.bind.annotation.*;

import com.fit.demo.Users.entidades.User;
import com.fit.demo.Users.repositry.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return repository.save(user);
    }
}