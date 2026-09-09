package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.AuditLogDTO;
import com.example.Spring_Salon_Project.dto.UserDTO;
import com.example.Spring_Salon_Project.entity.SaloonService;
import com.example.Spring_Salon_Project.entity.User;
import com.example.Spring_Salon_Project.enumiration.ServiceStatus;
import com.example.Spring_Salon_Project.enumiration.UserStatus;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.UserRepository;
import com.example.Spring_Salon_Project.service.AuditLogService;
import com.example.Spring_Salon_Project.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        log.info("Execute method saveUser");

        try {
            User user = new User();
            user.setUserName(userDTO.getUserName());
            user.setUserRole(userDTO.getUserRole());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            if (userDTO.getUserStatus() != null) {
                user.setUserStatus(userDTO.getUserStatus());
            } else {
                user.setUserStatus(UserStatus.ACTIVE);
            }

            User save = userRepository.save(user);

            log.info("User saved successfully");

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("CREATE");
            logDTO.setEntityName("USER");
            logDTO.setEntityId(save.getUserId());
            logDTO.setPerformedBy(save.getUserName());
            logDTO.setDetails("New user created: " + save.getUserName());
            auditLogService.saveAuditLog(logDTO);

            return new UserDTO(save.getUserId(), save.getUserName(), save.getUserRole(),
                    null, save.getUserStatus(), user.getEmail());

        } catch (Exception e) {
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

//        return userRepository.selectUser(userId);
        UserDTO userDTO = userRepository.selectUser(userId);
        if (userDTO == null) {
            throw new CustomerException(404, "Customer not found for ID: " + userId);
        }
        return userDTO;
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

        AuditLogDTO logDTO = new AuditLogDTO();
        logDTO.setAction("UPDATE");
        logDTO.setEntityName("USER");
        logDTO.setEntityId(user.getUserId());
        logDTO.setPerformedBy(user.getUserName());
        logDTO.setDetails("User updated: " + user.getUserName());
        auditLogService.saveAuditLog(logDTO);
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

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("DELETE");
            logDTO.setEntityName("USER");
            logDTO.setEntityId(user.getUserId());
            logDTO.setPerformedBy(user.getUserName());
            logDTO.setDetails("User soft-deleted (INACTIVE): " + user.getUserName());
            auditLogService.saveAuditLog(logDTO);

        } catch (Exception e) {
            log.error("Error in method deleteUser() : {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void changeSaloonStatus(long userId) {
        log.info("Execute method changeSaloonStatus");
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new CustomerException(404, "User not found ID:" + userId);
        }
        User user = optionalUser.get();
        if (user.getUserStatus() == UserStatus.ACTIVE) {
            user.setUserStatus(UserStatus.INACTIVE);
        } else {
            user.setUserStatus(UserStatus.ACTIVE);
        }
        userRepository.save(user);
        log.info("User status changed successfully");

        AuditLogDTO logDTO = new AuditLogDTO();
        logDTO.setAction("UPDATE");
        logDTO.setEntityName("USER");
        logDTO.setEntityId(user.getUserId());
        logDTO.setPerformedBy("admin");
        logDTO.setDetails("User status changed to: " + user.getUserStatus());
        auditLogService.saveAuditLog(logDTO);
    }
}
