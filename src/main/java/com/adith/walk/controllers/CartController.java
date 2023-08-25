package com.adith.walk.controllers;

import com.adith.walk.Entities.Address;
import com.adith.walk.Entities.CartItem;
import com.adith.walk.Entities.Customer;
import com.adith.walk.dto.AddressRequest;
import com.adith.walk.helper.Message;
import com.adith.walk.service.*;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/user/cart")
public class CartController {


    ProductService productService;
    CartService cartService;
    CustomerService customerService;

    CartItemService cartItemService;



    AddressService addressService;

    public CartController( AddressService addressService,
                            ProductService productService,
                          CartService cartService,
                          CustomerService customerService,
                           CartItemService cartItemService) {
        this.productService = productService;
        this.cartService = cartService;
        this.customerService = customerService;
        this.addressService = addressService;
        this.cartItemService=cartItemService;
    }




    @GetMapping()
    public String getCart(@ModelAttribute("newAddress") AddressRequest address, Model model, Principal principal){

        Customer customer=customerService.getCustomerByMobile(principal.getName());
        model.addAttribute("cart",cartService.getCartByCustomerId(customer.getUserId()));

            model.addAttribute("defaultAddress",addressService.getDefaultAddress(principal));
            model.addAttribute("addressList",addressService.getAllAddress(principal));

        return "user/cart";
    }

    @PostMapping("address/add")
    public String addNewAddress(@Valid @ModelAttribute("newAddress")AddressRequest address, BindingResult validationResult, Model model,Principal principal){
        if(validationResult.hasErrors()){
            return "redirect:/user/cart";
        }

        addressService.save(address,principal);

        return "redirect:/user/cart";
    }

    @PostMapping("add/{productId}")
    public String addToCart(@PathVariable Integer productId, Principal principal, Model model){


        try {
            cartService.add(productId, principal);
        } catch (AlreadyUsedException e) {
           model.addAttribute("message",new Message(e.getMessage(),"alert-danger"));
            return "redirect:/products/"+productId;
        }
        return "redirect:/products/"+productId;


    }

    @DeleteMapping("delete/{itemId}")
    public String deleteProduct(@PathVariable Long itemId){

        cartService.deleteItem(itemId);
        return "redirect:/user/cart";
    }

    @PutMapping("/add/quantity/{itemId}")
    public String addQuantity(@PathVariable Long itemId){

        cartItemService.addQuantity(itemId);

        return "redirect:/user/cart";
    }


    @PutMapping("/remove/quantity/{itemId}")
    public String removeQuantity(@PathVariable Long itemId){

        cartItemService.removeQuantity(itemId);

        return "redirect:/user/cart";
    }
}
