package com.adith.walk.controllers;


import com.adith.walk.Entities.Product;
import com.adith.walk.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Controller
@RequestMapping("products")
public class ProductController {

    @Autowired
    ProductService productService;

    //produces = MediaType.APPLICATION_JSON_VALUE
    @GetMapping("page/{pageNumber}")
    public String getProductPage(@PathVariable Integer pageNumber, Model model){
        model.addAttribute("products",productService.getProductByPage(pageNumber,12));

        return "public/products";
    }


    @GetMapping("{id}")
    public String  getProduct(@PathVariable Integer id, Model model){

        model.addAttribute("product",productService.getProductById(id));

        return "public/product";
    }

    @GetMapping("category/{category}")
    public List<Product>getAllProductByCategory(@PathVariable String category){

        return productService.getAllProductsByCategory(category);
    }



}
