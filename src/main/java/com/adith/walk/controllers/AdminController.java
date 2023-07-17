package com.adith.walk.controllers;

import com.adith.walk.Entities.Coupon;
import com.adith.walk.Entities.Customer;
import com.adith.walk.Entities.Product;
import com.adith.walk.service.CouponService;
import com.adith.walk.service.CustomerService;
import com.adith.walk.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    CustomerService  customerService;
    @Autowired
    ProductService productService;
    @Autowired
    CouponService couponService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("login")
    public String getAdminLoginPage(){
            return "admin/login";
    }


    @GetMapping("dashboard")
    public String getAdminDashboard(){
        return "admin/dashboard" ;
    }


    @GetMapping("/users")
    public String getFirstPageOfCustomers(Model model){

        model.addAttribute("users",customerService.getPageOfCustomer(1,10));
        return "admin/userList";

    }

    @GetMapping("/users/{pageNumber}")
    public String getPageOfCustomers(@PathVariable Integer pageNumber, Model model){

        model.addAttribute("users",customerService.getPageOfCustomer(pageNumber,10));
        return "admin/userList";

    }


    @ResponseBody
    @PostMapping("users/add-user")
    public Customer  addUser(@RequestBody Customer customer){

        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
        return  customerService.saveCustomer(customer);
    }


    @ResponseBody
    @GetMapping("products")
    public List<Product>getAllProducts(){

        return productService.getAllProducts();
    }

    @GetMapping("products/add-product")
    public String addProduct(){
        return "admin/addProduct";
    }

    @ResponseBody
    @GetMapping("coupons")
    public List<Coupon> getAllCoupons(){

        return couponService.getAllCoupons();
    }


    @GetMapping("add-coupons")
    public String addCoupons(){

        return "admin/addCoupon";
    }



}

