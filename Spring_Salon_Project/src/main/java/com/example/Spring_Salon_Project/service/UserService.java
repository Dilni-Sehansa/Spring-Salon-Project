package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.UserDTO;

public interface UserService {
    void saveUser(UserDTO userDTO);
    UserDTO getUserDetails(String userName, String password);

}
