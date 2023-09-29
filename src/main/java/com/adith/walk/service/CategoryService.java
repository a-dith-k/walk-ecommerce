package com.adith.walk.service;

import com.adith.walk.entities.ProductCategory;
import com.adith.walk.repositories.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    public void save(ProductCategory category) {
        categoryRepo.save(category);
    }


    public List<ProductCategory> getAllCategories() {

        return categoryRepo.findAll();
    }


    public ProductCategory getCategoryById(Integer id) {
        return categoryRepo.findById(id).orElse(new ProductCategory());
    }
}
