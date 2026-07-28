package com.eku.proj1.docdigest.service;

import com.eku.proj1.docdigest.dto.RegisterRequest;
import com.eku.proj1.docdigest.dto.RegisterResponse;
import com.eku.proj1.docdigest.entity.User;

public interface UserService {
     RegisterResponse registerUser(RegisterRequest registerRequest);

     User loginUser(String email, String password);

     User getCurrentUser();
}
