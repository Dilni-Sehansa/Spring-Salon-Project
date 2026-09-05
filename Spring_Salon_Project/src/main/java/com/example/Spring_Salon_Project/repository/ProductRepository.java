package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
}
