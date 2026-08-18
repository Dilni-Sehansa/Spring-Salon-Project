package com.example.Spring_Salon_Project.security;


import com.example.Spring_Salon_Project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.example.Spring_Salon_Project.entity.User> optionalUser = userRepository.findByUserName(username);

        if(optionalUser.isEmpty())
            throw new RuntimeException("Sorry no user");


        String userRolesStr = String.valueOf(optionalUser.get().getUserRole());//string.valueOf


        String[] roles = new String[0];
        if (userRolesStr != null && !userRolesStr.trim().isEmpty()) {
            roles = Arrays.stream(userRolesStr.split(","))
                    .map(String::trim)
                    .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                    .filter(role -> !role.isEmpty())
                    .toArray(String[]::new);
        }



        return User.builder()
                .username(optionalUser.get().getUserName())
                .password(optionalUser.get().getPassword())
                .roles(roles)
                .build();

    }

}
