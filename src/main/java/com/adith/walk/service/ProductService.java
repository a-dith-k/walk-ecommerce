package com.adith.walk.service;


import com.adith.walk.Entities.Product;
import com.adith.walk.repositories.ProductPagination;
import com.adith.walk.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPagination productPagination;

    public List<Product>getAllProducts(){

        return productRepository.findAll();
    }

    public List<Product> getAllProductsByCategory(String category) {

        return productRepository.findByCategoryId(category);

    }

    public Product getProductById(Integer id) {

        Optional<Product> byId = productRepository.findById(id);
        Product product = byId.get();
        return product;
    }

    public List<Product> getProductByPage(Integer pageNumber, Integer count) {
        Page<Product> all = productPagination.findAll(PageRequest.of(pageNumber, count));
        List<Product> list=new ArrayList<>();
        all.stream().forEach(product -> list.add(product));
        return list;
    }
}
