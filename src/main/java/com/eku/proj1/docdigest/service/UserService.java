package com.eku.proj1.docdigest.service;

import com.eku.proj1.docdigest.entity.User;

public interface UserService {
     User registerUser(String name,String email,String password);

     User loginUser(String email, String password);

     User getCurrentUser();
}
