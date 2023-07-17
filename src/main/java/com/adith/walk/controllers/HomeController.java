package com.adith.walk.controllers;

import com.adith.walk.repositories.CustomerRepository;
import com.adith.walk.service.AdminService;
import com.adith.walk.service.CustomerService;
import com.adith.walk.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AdminService adminService;

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductService productService;

    @GetMapping
    public String  getHome(Model model){

        model.addAttribute("products",productService.getProductByPage(1,16));
        return "public/home";
    }




}
