package com.adith.walk.repositories;


import com.adith.walk.Entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    //    List<Product> findByCategoryId(String category);


    Product removeProductByProductId(Integer id);


    List<Product> findByProductNameContaining(String name);


    Product findByProductName(String name);


}
