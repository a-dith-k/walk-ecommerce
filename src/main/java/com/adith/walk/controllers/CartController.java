package com.adith.walk.controllers;

import com.adith.walk.dto.AddressRequest;
import com.adith.walk.entities.Customer;
import com.adith.walk.helper.Message;
import com.adith.walk.service.CartItemService;
import com.adith.walk.service.CustomerService;
import com.adith.walk.service.ProductService;
import com.adith.walk.service.address.service.AddressServiceImpl;
import com.adith.walk.service.cart.service.CartService;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import jakarta.validation.Valid;
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


    AddressServiceImpl addressServiceImpl;

    public CartController(AddressServiceImpl addressServiceImpl,
                          ProductService productService,
                          CartService cartService,
                          CustomerService customerService,
                          CartItemService cartItemService) {
        this.productService = productService;
        this.cartService = cartService;
        this.customerService = customerService;
        this.addressServiceImpl = addressServiceImpl;
        this.cartItemService = cartItemService;
    }


    @GetMapping()
    public String getCart(@ModelAttribute("newAddress") AddressRequest address, Model model, Principal principal) {

        Customer customer = customerService.getCustomerByMobile(principal.getName());
        model.addAttribute("cart", cartService.getCartByCustomerId(customer.getUserId()));

        model.addAttribute("defaultAddress", addressServiceImpl.getDefaultAddress(principal));
        model.addAttribute("addressList", addressServiceImpl.getAllAddress(principal));

        return "user/cart";
    }

    @PostMapping("address/add")
    public String addNewAddress(@Valid @ModelAttribute("newAddress") AddressRequest address, BindingResult validationResult, Model model, Principal principal) {
        if (validationResult.hasErrors()) {
            return "redirect:/user/cart";
        }

        addressServiceImpl.save(address, principal);

        return "redirect:/user/cart";
    }

    @PostMapping("add/{productId}")
    public String addToCart(@PathVariable Integer productId, @RequestParam("sizeId") Long sizeId, Principal principal, Model model) {


        try {
            cartService.add(productId, sizeId, principal);
        } catch (AlreadyUsedException e) {
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            return "redirect:/products/" + productId;
        }
        return "redirect:/products/" + productId;


    }

    @DeleteMapping("delete/{itemId}")
    public String deleteProduct(@PathVariable Long itemId) {

        cartService.deleteItem(itemId);

        return "redirect:/user/cart";
    }

    @PutMapping("/add/quantity/{itemId}")
    public String addQuantity(@PathVariable Long itemId) {

        cartItemService.addQuantity(itemId);

        return "redirect:/user/cart";
    }


    @PutMapping("/remove/quantity/{itemId}")
    public String removeQuantity(@PathVariable Long itemId) {

        cartItemService.removeQuantity(itemId);

        return "redirect:/user/cart";
    }
}
