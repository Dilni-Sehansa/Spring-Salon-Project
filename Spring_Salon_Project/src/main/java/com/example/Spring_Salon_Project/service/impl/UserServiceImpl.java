package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.UserDTO;
import com.example.Spring_Salon_Project.entity.User;
import com.example.Spring_Salon_Project.repository.UserRepository;
import com.example.Spring_Salon_Project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void saveUser(UserDTO userDTO) {
        log.info("Execute method saveUser");

        try{

            User user = new User();
            user.setUserName(userDTO.getUserName());
            user.setUserRole(userDTO.getUserRole());
            user.setPassword(userDTO.getPassword());
        }catch (Exception e){
            log.info("Error saving user");
            throw e;
        }

    }

    @Override
    public UserDTO getUserDetails(String userName, String password) {
        log.info("Execute method getUserDetails");

        try{
            Optional<User> optionUser = userRepository.findUserNameAndPassword(userName,password);

            if(optionUser.isEmpty())
                throw new RuntimeException(("Sorry, user not found"));

            User user = optionUser.get();
            return new UserDTO(user.getUserId(),user.getUserName(),user.getUserRole(),user.getPassword());

        }catch (Exception e){
            log.info("Error getting user");
            throw e;
        }
    }
}
