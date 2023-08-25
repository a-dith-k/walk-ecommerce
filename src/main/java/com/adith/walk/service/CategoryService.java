package com.adith.walk.service;

import com.adith.walk.Entities.Category;
import com.adith.walk.repositories.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    public void save(Category category){
        categoryRepo.save(category);
    }



}
