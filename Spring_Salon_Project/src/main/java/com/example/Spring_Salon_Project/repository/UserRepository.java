package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserNameAndPassword(String userName, String password);
}
