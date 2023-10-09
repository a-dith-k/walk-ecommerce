package com.adith.walk.controllers;


import com.adith.walk.dto.ConfirmTokenRequest;
import com.adith.walk.dto.CustomerRegistrationRequest;
import com.adith.walk.helper.Message;
import com.adith.walk.service.CustomerService;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    final CustomerService customerService;
    final Logger logger;

    RegistrationController(CustomerService customerService) {
        this.customerService = customerService;

        logger = LoggerFactory.getLogger(RegistrationController.class);
    }


    @GetMapping("register")
    public String getCustomerRegistrationView(@ModelAttribute("request") CustomerRegistrationRequest request, Model model) {


        return "registration/user-registration";
    }

    @PostMapping("register")
    public String registerCustomer(@Valid @ModelAttribute("request") CustomerRegistrationRequest request, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("request", request);
            return "registration/user-registration";

        }
        try {
            customerService.registerCustomer(request);
        } catch (AlreadyUsedException e) {
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            return "registration/user-registration";
        }


        return "registration/confirm";
    }


    @GetMapping("register/confirm")
    public String registerConfirmation(@ModelAttribute("token") ConfirmTokenRequest token, @ModelAttribute("request") CustomerRegistrationRequest registrationRequest) {
        return "registration/confirm";
    }

    @PostMapping("register/confirm")
    public String registerConfirmation(@ModelAttribute("token") ConfirmTokenRequest token, Model model) {
        logger.info(token.getToken());
        logger.info(token.getMobile());
        try {
            if (customerService.confirmToken(token)) {

                return "redirect:/user/login";
            } else {
                return "registration/confirm";
            }
        } catch (Exception e) {
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            return "registration/confirm";
        }
    }


    @GetMapping("confirm-token")
    public String confirmToken(@ModelAttribute("token") ConfirmTokenRequest confirmTokenRequest) {

        logger.info("Customer 2nd stage Mobile:{}", confirmTokenRequest.getMobile());


        return "registration/confirm-token";
    }


    @PostMapping("confirm-token")
    public String confirmTokenPOST(@ModelAttribute("token") ConfirmTokenRequest token, Model model) {

        logger.info(token.getToken());
        logger.info(token.getMobile());
        try {
            if (customerService.confirmToken(token)) {

                return "redirect:/user/login";
            } else {
                return "registration/confirm-token";
            }
        } catch (Exception e) {
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            return "registration/confirm-token";
        }

    }


    @GetMapping("/send-otp")
    public String resendOTP(@ModelAttribute("token") ConfirmTokenRequest tokenRequest) {

        return "registration/send-otp";
    }

    @PostMapping("/send-otp")
    public String resendOTP(@ModelAttribute("token") ConfirmTokenRequest tokenRequest, Model model) {
        logger.info("Customer Mobile:{}", tokenRequest.getMobile());
//
        try {
            customerService.sendNewToken(tokenRequest.getMobile());
        } catch (UsernameNotFoundException e) {
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            return "registration/send-otp";
        }

        return "registration/confirm-token";
    }

}


//
//

//    ----------------------------------------------------------------------------------
//
//    @GetMapping("/confirm")
//    public String confirmMobile(){
//
//        return "registration/re-confirmation";
//    }
//
//    @PostMapping("/confirm")
//    public String getConfirm(@RequestParam String mobile,Model model){
//        try{
//            customerService.sendNewToken(mobile);
//        }catch (UsernameNotFoundException une){
//            model.addAttribute("message",new Message(une.getMessage(),"alert-danger"));
//            return "registration/re-confirmation";
//
//        }
//
//        ConfirmTokenRequest tokenRequest=new ConfirmTokenRequest();
//        CustomerRegistrationRequest request=new CustomerRegistrationRequest();
//        request.setMobileNumber(mobile);
//        tokenRequest.setMobile(mobile);
//        model.addAttribute("token",tokenRequest);
//        model.addAttribute("request",request);
//
//        return "registration/confirm-token";
//    }
//
//}
