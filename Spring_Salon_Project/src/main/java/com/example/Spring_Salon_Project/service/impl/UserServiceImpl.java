package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.UserDTO;
import com.example.Spring_Salon_Project.entity.User;
import com.example.Spring_Salon_Project.enumiration.UserStatus;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.UserRepository;
import com.example.Spring_Salon_Project.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        log.info("Execute method saveUser");

        try{

            User user = new User();
            user.setUserName(userDTO.getUserName());
            user.setUserRole(userDTO.getUserRole());
            user.setEmail(userDTO.getEmail());
//            user.setPassword(userDTO.getPassword());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setUserStatus(UserStatus.ACTIVE);

            User save = userRepository.save(user);

            log.info("User saved successfully");

//            return new UserDTO(save.getUserId(),save.getUserName(),save.getUserRole(),save.getPassword());
            return new UserDTO(save.getUserId(),save.getUserName(),save.getUserRole(),null,save.getUserStatus(),user.getEmail());

        }catch (Exception e){
            log.info("Error saving user");
            throw e;
        }

    }

    @Override
    public UserDTO getUserDetails(String userName, String password) {
        log.info("Execute method getUserDetails");

        try{
//            Optional<User> optionUser = userRepository.findByUserNameAndPassword(userName,password);
            Optional<User> optionUser = userRepository.findByUserName(userName);


            if(optionUser.isEmpty())
                throw new CustomerException(404,"Sorry, user not found");

            User user = optionUser.get();

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new CustomerException(404,"Invalid Password");

            }

            if (user.getUserStatus() == UserStatus.INACTIVE) {
                throw new CustomerException(404,"Your account is suspended/Inactive");
            }
//            return new UserDTO(user.getUserId(),user.getUserName(),user.getUserRole(),user.getPassword());
            return new UserDTO(user.getUserId(),user.getUserName(),user.getUserRole(),null,user.getUserStatus(),user.getEmail());


        }catch (Exception e){
            log.info("Error getting user");
            throw e;
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public List<UserDTO> filterUsers(String username) {
        return userRepository.filterUser(username);
    }

    @Override
    public UserDTO selectUser(long userId) {
        return userRepository.selectUser(userId);
    }

    @Override
    public void updateUser(UserDTO userDTO) {

        Optional<User> optionalUser = userRepository.findById(userDTO.getUserId());

        if(optionalUser.isEmpty())
            throw new CustomerException(404,"Sorry, user not found");

        User user = optionalUser.get();
        user.setUserName(userDTO.getUserName());
//        user.setUserRole(userDTO.getUserRole());

        if (userDTO.getEmail() != null &&
                !userDTO.getEmail().trim().isEmpty()) {

            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getUserRole() != null) {
            user.setUserRole(userDTO.getUserRole());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        if (userDTO.getUserStatus() != null) {
            user.setUserStatus(userDTO.getUserStatus());
        }

        userRepository.save(user);
    }


    @Override
    @Transactional
    public void deleteUser(long userId) {

        log.info("Execute method deleteUser() id{}", userId);

        try {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isEmpty())
                throw new CustomerException(404,"Sorry, related user is not found.");


            User user = userOptional.get();
            user.setUserStatus(UserStatus.INACTIVE);
            userRepository.save(user);

            log.info("User Status Changed Successfully");

        } catch (Exception e) {
            log.error("Error in method deleteUser() : {}", e.getMessage());
            throw e;
        }
    }
}
