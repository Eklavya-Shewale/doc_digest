package com.eku.proj1.docdigest.service.impl;

import com.eku.proj1.docdigest.dto.RegisterRequest;
import com.eku.proj1.docdigest.dto.RegisterResponse;
import com.eku.proj1.docdigest.entity.User;
import com.eku.proj1.docdigest.repository.UserRepository;
import com.eku.proj1.docdigest.service.UserService;
import jdk.jshell.spi.ExecutionControl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public RegisterResponse registerUser(RegisterRequest userDTO) {
        User user = new User();
        ModelMapper mapper = new ModelMapper();

        mapper.map(userDTO, user);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        //TODO: Encode password before saving



        User savedUser = userRepository.save(user);

        return mapper.map(savedUser, RegisterResponse.class);
    }

    @Override
    public User loginUser(String email, String password) {
        return null;
    }

    @Override
    public User getCurrentUser() {
        return null;
    }
}
