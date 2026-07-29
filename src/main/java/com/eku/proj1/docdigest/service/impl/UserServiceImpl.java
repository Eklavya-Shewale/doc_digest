package com.eku.proj1.docdigest.service.impl;

import com.eku.proj1.docdigest.dto.LoginRequest;
import com.eku.proj1.docdigest.dto.LoginResponse;
import com.eku.proj1.docdigest.dto.RegisterRequest;
import com.eku.proj1.docdigest.dto.RegisterResponse;
import com.eku.proj1.docdigest.entity.User;
import com.eku.proj1.docdigest.exception.EmailAlreadyExistsException;
import com.eku.proj1.docdigest.exception.UserNotFoundException;
import com.eku.proj1.docdigest.repository.UserRepository;
import com.eku.proj1.docdigest.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResponse registerUser(RegisterRequest userDTO) {
        User user = new User();


        modelMapper.map(userDTO, user);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, RegisterResponse.class);
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new UserNotFoundException("User does not exist"));
        return new LoginResponse("Login successful");
    }

    @Override
    public User getCurrentUser() {
        return null;
    }
}
