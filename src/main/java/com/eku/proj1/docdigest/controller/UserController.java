package com.eku.proj1.docdigest.controller;

import com.eku.proj1.docdigest.entity.User;
import com.eku.proj1.docdigest.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<User> registerUser(UserDTO userDTO){

    }
}
