package com.adith.walk.controllers;

import com.adith.walk.Entities.Wishlist;
import com.adith.walk.dto.OtpLoginRequestDto;
import com.adith.walk.service.CustomerService;
import com.adith.walk.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

@RequestMapping("user")
public class UserController {

    @Autowired
    CustomerService customerService;

    @Autowired
    WishlistService wishlistService;

    @GetMapping("login")
    public String getLogin(@SessionAttribute("request")OtpLoginRequestDto request, HttpSession session){
        session.setAttribute("request",request);


        return "public/login";
    }


    @GetMapping("profile")
    public String getUserDashBoard(){

        return "user/dashboard";
    }

    @GetMapping("wishlist/{userId}")
    public List<Wishlist> getUserWishList(@PathVariable Integer userId){

        return wishlistService.getAllWisListItems(userId);
    }

    @GetMapping("orders")
    public String getUserOrders(){

        return "user/orders";
    }


    @GetMapping("addresses")
    public String getUserAddresses(){

        return "user/addresses";
    }

    @GetMapping("coupons")
    public String getUserCoupons(){

        return "user/coupons";
    }

    @PostMapping("logout")
    public String getLogout(){

        return "public/home";
    }

    @GetMapping("cart")
    public String getCart(){

        return "user/cart";
    }

    @GetMapping("checkout")
    public String GetCheckoutPage(){

        return "user/checkout";
    }

    @GetMapping("success")
    public String GetSuccessPage(){

        return "success";
    }


}
