package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.dto.CustomerDTO;
import com.example.Spring_Salon_Project.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findByCustomerName(String customerName);

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.CustomerDTO(
          c.customerId,
          c.customerName,
          c.phone,
          c.customerStatus,
          u.userId
      )
      FROM Customer c LEFT JOIN c.user u ORDER BY c.customerId DESC 
      """)
    List<CustomerDTO> getAllCustomers();


    @Query("""
    SELECT new com.example.Spring_Salon_Project.dto.CustomerDTO(
        c.customerId,
        c.customerName,
        c.phone,
        c.customerStatus,
        u.userId
    )
    FROM Customer c
    LEFT JOIN c.user u
    WHERE :customerName IS NULL 
       OR :customerName = ''
       OR LOWER(c.customerName) LIKE LOWER(CONCAT('%', :customerName, '%'))
       OR c.phone LIKE CONCAT('%', :customerName, '%')
       OR LOWER(CAST(c.customerStatus AS string)) LIKE LOWER(CONCAT('%', :customerName, '%'))
    ORDER BY c.customerId DESC
    """)
    List<CustomerDTO> filterCustomers(@Param("customerName") String customerName);

    @Query("""
         SELECT new com.example.Spring_Salon_Project.dto.CustomerDTO(
              c.customerId,
              c.customerName,
              c.phone,
              c.customerStatus,
              u.userId
         )
         FROM Customer c LEFT JOIN c.user u WHERE c.customerId=?1
         """)
    CustomerDTO selectCustomer(Long customerId);

    @Query("""
         SELECT new com.example.Spring_Salon_Project.dto.CustomerDTO(
              c.customerId,
              c.customerName,
              c.phone,
              c.customerStatus,
              u.userId
         )
         FROM Customer c LEFT JOIN c.user u WHERE u.userId = :userId
         """)
    Optional<CustomerDTO> getCustomerByUserId(@Param("userId") long userId);

    @Query("""
          SELECT new com.example.Spring_Salon_Project.dto.CustomerDTO(
               c.customerId, c.customerName, c.phone, c.customerStatus, u.userId
          )
          FROM Customer c LEFT JOIN c.user u WHERE c.phone = :phone
         """)
         Optional<CustomerDTO> findByPhone(@Param("phone") String phone);
}
