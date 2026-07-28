package com.eku.proj1.docdigest.service.impl;

import com.eku.proj1.docdigest.entity.User;
import com.eku.proj1.docdigest.repository.UserRepository;
import com.eku.proj1.docdigest.service.UserService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(String name, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        //TODO: Encode password before saving
        user.setPassword(password);


        userRepository.save(user);
        return user;
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
