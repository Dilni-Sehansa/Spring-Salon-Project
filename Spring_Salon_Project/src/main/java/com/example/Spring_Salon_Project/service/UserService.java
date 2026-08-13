package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO saveUser(UserDTO userDTO);
    UserDTO getUserDetails(String userName, String password);

    List<UserDTO> getAllUsers();

    List<UserDTO> filterUsers(String username);

    UserDTO selectUser(long userId);

    void updateUser(UserDTO userDTO);

    void deleteUser(long userId);
}
