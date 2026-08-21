package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.dto.UserDTO;
import com.example.Spring_Salon_Project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByUserNameAndPassword(String userName, String password);

    Optional<User> findByUserName(String username);
//    @Query(value = "SELECT new com.example.Spring_Salon_Project.dto.UserDTO(u.userId,u.userName,u.userRole) " +
//            "FROM User u")


    @Query("""
            SELECT new com.example.Spring_Salon_Project.dto.UserDTO(
                u.userId,
                u.userName,
                u.userRole,
                u.password,
                u.userStatus,
                u.email
            )
            FROM User u
            ORDER BY u.userId DESC
            """)
    List<UserDTO> getAllUsers();


//    @Query(value = "SELECT new com.example.Spring_Salon_Project.dto.UserDTO(u.userId,u.userName,u.userRole) " +
//            "FROM User u " +
//            "WHERE (?1 IS NULL OR u.userName LIKE %?1%)")

//    @Query(value = "SELECT new com.example.Spring_Salon_Project.dto.UserDTO(u.userId,u.userName,u.userRole,u.password,u.userStatus,u.email) " +
//            "FROM User u " +
//            "WHERE (:username IS NULL OR LOWER(u.userName) LIKE LOWER(CONCAT('%', :username, '%')))")




    @Query("""
            SELECT new com.example.Spring_Salon_Project.dto.UserDTO(
                u.userId,
                u.userName,
                u.userRole,
                u.password,
                u.userStatus,
                u.email
            )
            FROM User u
            WHERE :username IS NULL
               OR :username = ''
               OR LOWER(u.userName) LIKE LOWER(CONCAT('%', :username, '%'))
               OR LOWER(u.email) LIKE LOWER(CONCAT('%', :username, '%'))
            ORDER BY u.userId DESC
            """)
    List<UserDTO> filterUser(@Param("username") String username);

//    @Query(value = "SELECT new com.example.Spring_Salon_Project.dto.UserDTO(u.userId,u.userName,u.userRole,u.password,u.userStatus,u.email) " +
//            "FROM User u WHERE u.userId=?1")

    @Query("""
            SELECT new com.example.Spring_Salon_Project.dto.UserDTO(
                u.userId,
                u.userName,
                u.userRole,
                u.password,
                u.userStatus,
                u.email
            )
            FROM User u
            WHERE u.userId = :userId
            """)
    UserDTO selectUser(long userId);
}
