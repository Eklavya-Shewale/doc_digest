package com.eku.proj1.docdigest.controller;

import com.eku.proj1.docdigest.dto.LoginRequest;
import com.eku.proj1.docdigest.dto.LoginResponse;
import com.eku.proj1.docdigest.dto.RegisterRequest;
import com.eku.proj1.docdigest.dto.RegisterResponse;
import com.eku.proj1.docdigest.entity.User;
import com.eku.proj1.docdigest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest userDTO)
    {
        RegisterResponse registeredUser = userService.registerUser(userDTO);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("========== LOGIN CONTROLLER HIT ==========");

        LoginResponse loginResponse = userService.loginUser(loginRequest);

        return ResponseEntity.ok(loginResponse);
    }
}
