package com.adith.walk.repositories;


import com.adith.walk.entities.Product;
import com.adith.walk.enums.CustomerCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    //    List<Product> findByCategoryId(String category);


    Product removeProductByProductId(Integer id);


    List<Product> findByProductNameContaining(String name);


    Product findByProductName(String name);


    Page<Product> findProductsByCustomerCategory(CustomerCategory customerCategory, Pageable pageable);


}
