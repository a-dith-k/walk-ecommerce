package com.adith.walk.controllers;

import com.adith.walk.Entities.Address;
import com.adith.walk.Entities.Customer;
import com.adith.walk.Entities.Orders;
import com.adith.walk.Entities.Payment;
import com.adith.walk.dto.AddressRequest;
import com.adith.walk.dto.CustomerProfileUpdateRequest;
import com.adith.walk.dto.PasswordResetRequest;
import com.adith.walk.exceptions.CouponNotFoundException;
import com.adith.walk.exceptions.InvalidCouponException;
import com.adith.walk.helper.Message;
import com.adith.walk.service.*;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@SessionAttributes({"activeUser","message"})
@RequestMapping("user")
public class UserController {


    final Logger logger;
    final CustomerService customerService;
    final WishlistService wishlistService;
    final ModelMapper modelMapper;

    final AddressService addressService;
    final CartService cartService;

    final PaymentService paymentService;

    final OrderService orderService;

    final ProductService productService;

    final CouponService couponService;
    UserController(OrderService orderService, PaymentService paymentService, CustomerService customerService, WishlistService wishlistService, ModelMapper modelMapper, CartService cartService, AddressService addressService, ProductService productService, CouponService couponService){
        this.productService = productService;
        this.couponService = couponService;
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


    model.addAttribute("orders",orderService.getAllOrderByPrincipal(principal));
            return "user/orders";
    }

    @GetMapping("orders/{orderId}")
    public String getUserOrders(@PathVariable Long orderId, Model model){

        model.addAttribute("order",orderService.getOrderById(orderId));
        model.addAttribute("orderStatus",orderService.getOrderStatus(orderId));
        return "user/order";
    }

    @PutMapping("orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId){

        orderService.cancelOrder(orderId);

        return "redirect:/user/orders";
    }

    @PutMapping("orders/{orderId}/return")
    public String returnOrder(@PathVariable Long orderId){

        orderService.returnOrder(orderId);

        return "redirect:/user/orders";
    }



    @GetMapping("coupons")
    public String getUserCoupons(){

        return "user/coupons";
    }


    @GetMapping("checkout")
    public String GetCheckoutPage(Principal principal,Model model,HttpSession session){


        model.addAttribute("addressList",addressService.getAllAddress(principal));

        return "user/checkout";
    }


    @PostMapping("checkout")
    public String PostCheckoutPage(Principal principal,Model model,HttpSession session){


        model.addAttribute("addressList",addressService.getAllAddress(principal));
        orderService.createOrder(cartService.getCartByPrincipal(principal), principal, session);

        return "user/checkout";
    }

    @PostMapping("checkout/{id}")
    public String buyNowProduct(@PathVariable Integer id,Principal principal,HttpSession session,Model model){


        model.addAttribute("addressList",addressService.getAllAddress(principal));
        orderService.buyNow(id,principal,session);
        return "/user/checkout";
    }

    @PutMapping("checkout/applyCoupon")
    public String applyCoupon(@SessionAttribute("order")Orders order, @RequestParam String couponName,HttpSession session,Model model){


        try {
            order.setDiscountedPrice(couponService.applyCoupon(order.getTotalPrice(),couponService.getCouponByName(couponName)));
            order.setCoupon(couponService.getCouponByName(couponName));
            model.addAttribute("message",new Message("Coupon Applied SuccessFully","alert-success"));
        } catch (CouponNotFoundException | InvalidCouponException e) {
            model.addAttribute("message",new Message(e.getMessage(),"alert-danger"));
        }

        session.setAttribute("order",order);
        return "redirect:/user/checkout";
    }

    @PutMapping("checkout/removeCoupon")
    public String removeCoupon(@SessionAttribute("order")Orders order,HttpSession session,Model model){



            order.setDiscountedPrice(couponService.removeCoupon(order.getDiscountedPrice(),order.getTotalPrice(),order.getCoupon()));
            order.setCoupon(null);
            model.addAttribute("message",new Message("Coupon removed SuccessFully","alert-success"));

        session.setAttribute("order",order);
        return "redirect:/user/checkout";
    }


    @PostMapping("payment")
    public String doPayment(@RequestParam String paymentMethod, Principal principal, @SessionAttribute("order")Orders orders){
        if(paymentMethod.equals("cod")){
            Payment payment = paymentService.doPayment(orders.getTotalPrice(), paymentMethod, "paid");
            orderService.placeOrder(orders,payment,principal);
            cartService.deleteCart(principal);
            return "/user/success";
        }

        return "redirect:/user/checkout";
    }

    @GetMapping("/order/success")
    public String getSuccessPage(){

        return "user/success";
    }

    @PutMapping("/checkout/deliveryAddress/update")
    public String updateCheckoutDeliveryAddress(@RequestParam Long addressId,Principal principal,HttpSession session){

        addressService.setDefault(addressId,principal);



        return "redirect:/user/checkout";
    }

    @PutMapping("/checkout/billingAddress/update")
    public String updateCheckoutBillingAddress(@RequestParam Long addressId,Principal principal){

        addressService.setBillingAddress(addressId,principal);

        return "redirect:/user/checkout";
    }


    @GetMapping("wishlist")
    public String getUserWishList(Principal principal,Model model){

      model.addAttribute("wishlist",  wishlistService.getWishlist(customerService.getCustomerByMobile(principal.getName())));

        return "user/wishlist";
    }

    @PostMapping("wishlist/add/{productId}")
    public String toggleToWishlist(@PathVariable Integer productId,Principal principal){


        wishlistService.toggleToWishlist(productId,principal);
        return "redirect:/products/"+productId;
    }

    @PutMapping("wishlist/remove/{productId}")
    public String  removeWishlistProduct(@PathVariable Integer productId,Principal principal){
        wishlistService.toggleToWishlist(productId,principal);
        return "redirect:/user/wishlist";
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
