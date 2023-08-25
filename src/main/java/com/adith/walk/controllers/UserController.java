package com.adith.walk.controllers;

import com.adith.walk.Entities.*;
import com.adith.walk.dto.AddressRequest;
import com.adith.walk.dto.CustomerDTO;
import com.adith.walk.dto.CustomerProfileUpdateRequest;
import com.adith.walk.dto.PasswordResetRequest;
import com.adith.walk.helper.Message;
import com.adith.walk.service.*;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes({"activeUser"})
@RequestMapping("user")
public class UserController {


    Logger logger;
    CustomerService customerService;
    WishlistService wishlistService;
    ModelMapper modelMapper;

    AddressService addressService;
    CartService cartService;

    PaymentService paymentService;

    OrderService orderService;

    UserController(OrderService orderService,PaymentService paymentService,CustomerService customerService,WishlistService wishlistService,ModelMapper modelMapper,CartService cartService,AddressService addressService){
        logger= LoggerFactory.getLogger(UserController.class);
        this.customerService=customerService;
        this.wishlistService=wishlistService;
        this.modelMapper=modelMapper;
        this.addressService=addressService;
        this.cartService=cartService;
        this.paymentService=paymentService;
        this.orderService=orderService;
    }


    @GetMapping("login")
    public String getLogin(Authentication authentication){

        if(authentication!=null){
            return "redirect:/user/profile";
        }
        return "public/login";
    }


    @GetMapping("profile")
    public String getUserDashBoard(HttpSession session, Model model, Principal principal){
        session.setAttribute("activeUser",customerService.getActiveCustomer(principal));
        model.addAttribute("activeUserDetails",customerService.getProfileData(principal));
        return "user/dashboard";
    }

    @PostMapping("update-profile")
    public String updateProfile(@Valid @ModelAttribute("activeUserDetails") CustomerProfileUpdateRequest request, BindingResult result, Model model,Principal principal){

        if(result.hasErrors()){
            logger.info("errors:{}",result.toString());
           return "user/dashboard";
        }


        try {
            customerService.updateProfileData(request,principal);
        } catch (AlreadyUsedException e) {
            model.addAttribute("message",new Message(e.getMessage(),"alert-success"));
            return "user/dashboard";
        }
        model.addAttribute("message",new Message("Profile Update Success Successful","alert-success"));
        return "redirect:/user/profile";
    }

    @GetMapping("addresses")
    public String getUserAddresses(Principal principal,Model model){

        model.addAttribute("userAddressList",addressService.getAllAddress(principal));
        return "user/addresses";
    }


    @GetMapping("addresses/add")
    public String addAddressRequest(@ModelAttribute("newAddress") AddressRequest addressRequest){

        return "user/addAddress";
    }

    @PostMapping("addresses/add")
    public String addAddressResponse(@Valid @ModelAttribute("newAddress")AddressRequest addressRequest,BindingResult result,Model model,Principal principal){

        if(result.hasErrors()){
            model.addAttribute("message",new Message("Enter valid data","alert-danger"));
            return "user/addAddress";
        }

        addressService.save(addressRequest,principal);
        model.addAttribute("message",new Message("Address Successfully Added","alert-success"));
        return "redirect:/user/addresses";
    }

    @DeleteMapping("address/delete/{addressId}")
    public String deleteAddress(@PathVariable Long addressId){

        addressService.delete(addressId);

        return "redirect:/user/addresses";

    }

    @PutMapping("address/set-default/{addressId}")
    public String setDefaultAddress(@PathVariable Long addressId,Principal principal){

        addressService.setDefault(addressId,principal );

        return "redirect:/user/addresses";

    }


    @PostMapping("address/set-default")
    public String setDeliveryAddress(@RequestParam Long addressId,Principal principal){

        addressService.setDefault(addressId,principal );

        return "redirect:/user/cart";

    }


    @GetMapping("address/update/{addressId}")
    public  String updateAddress(@PathVariable Long addressId,Model model){
       new AddressRequest();
        Address address = addressService.addressById(addressId);
        AddressRequest addressRequest=new AddressRequest();
        addressRequest.setId(address.getId());
        modelMapper.map(address,addressRequest);
        model.addAttribute("addressUpdate",addressRequest);

        return "user/editAddress";

    }

    @PostMapping("address/update/{addressId}")
    public  String updateAddress(@Valid @ModelAttribute("addressUpdate")AddressRequest address , BindingResult result, Model model, Principal principal, @PathVariable Long addressId){

        if(result.hasErrors()){
            model.addAttribute("message",new Message("Enter valid data","alert-danger"));
            return "user/editAddress";
        }

        addressService.update(address);

        return "redirect:/user/addresses";

    }



    @GetMapping("orders")
    public String getUserOrders(Model model,Principal principal){


    model.addAttribute("orders",orderService.getAllOrderByPrincipal(principal))    ;
            return "user/orders";
    }

    @GetMapping("orders/{orderId}")
    public String getUserOrders(@PathVariable Long orderId, Model model,Principal principal){


        model.addAttribute("order",orderService.getOrderById(orderId));
        return "user/order";
    }

    @PutMapping("orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId){

        orderService.updateOrderStatus(orderId,"Cancelled");

        return "redirect:/user/orders";
    }



    @GetMapping("coupons")
    public String getUserCoupons(){

        return "user/coupons";
    }




    @GetMapping("checkout")
    public String GetCheckoutPage(Principal principal,Model model){

        model.addAttribute("deliveryAddress",addressService.getDefaultAddress(principal));
        model.addAttribute("cart",cartService.getCartByPrincipal(principal));
        model.addAttribute("addressList",addressService.getAllAddress(principal));
        model.addAttribute("billingAddress",addressService.getBillingAddress(principal));
        return "user/checkout";
    }

    @PostMapping("payment")
    public String doPayment(@RequestParam String paymentMethod,Principal principal){

        if(paymentMethod.equals("cod")){
            Cart cart = cartService.getCartByPrincipal(principal);
            Payment payment = paymentService.doPayment(cart.getTotalPrice(), paymentMethod, "paid");
            orderService.placeOrder(cart,payment,principal);
            cartService.deleteCart(principal);
            return "/user/success";
        }



        return "redirect:/user/checkout";
    }

    @PutMapping("/checkout/deliveryAddress/update")
    public String updateCheckoutDeliveryAddress(@RequestParam Long addressId,Principal principal){

        addressService.setDefault(addressId,principal);

        return "redirect:/user/checkout";
    }

    @PutMapping("/checkout/billingAddress/update")
    public String updateCheckoutBillingAddress(@RequestParam Long addressId,Principal principal){

        addressService.setBillingAddress(addressId,principal);

        return "redirect:/user/checkout";
    }


    @GetMapping("wishlist/{userId}")
    public String getUserWishList(@PathVariable Integer userId){

        return "user/wishlist";
    }

    @PostMapping("wishlist/add/{productId}")
    public String addToWishlist(@PathVariable Long productId){



        return "redirect:/products/"+productId;
    }

    //------------------------------------------------mappings for changing password-----------------------------------------------------

    @GetMapping("change-password")
    public String getChangePassword(@ModelAttribute("resetPasswordRequest")PasswordResetRequest passwordResetRequest){

        return "user/change-password";
    }

    @PostMapping("change-password")
    public String getChangePassword(@Valid @ModelAttribute("resetPasswordRequest")PasswordResetRequest passwordResetRequest, BindingResult bindingResult ,Model model,HttpSession session){

        if(bindingResult.hasErrors()){
            return "user/change-password";
        }
        try{
            Customer activeUser =(Customer) session.getAttribute("activeUser");

            logger.info("mobile: {}",activeUser.getMobileNumber());

            passwordResetRequest.setMobile(activeUser.getMobileNumber());
            customerService.resetPassword(passwordResetRequest);
        }catch (Exception e){
            model.addAttribute("message",new Message("Password Reset Successful","alert-success"));
            return "user/change-password";
        }


        model.addAttribute("message",new Message("Password Reset Successful","alert-success"));
        return "user/change-password";


    }




}
