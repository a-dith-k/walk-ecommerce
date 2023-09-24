package com.adith.walk.repositories;

import com.adith.walk.Entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<ProductCategory,Integer> {
}
