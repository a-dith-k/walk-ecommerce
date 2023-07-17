package com.adith.walk.controllers;


import com.adith.walk.Entities.Customer;
import com.adith.walk.dto.OtpLoginRequestDto;
import com.adith.walk.helper.Message;
import com.adith.walk.service.CustomerService;
import com.adith.walk.service.TwilioOtpService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class OTPController {

    Logger logger = LoggerFactory.getLogger(OTPController.class);
  
    public String OTP="";
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    TwilioOtpService twilioOtpService;
    @Autowired
    CustomerService customerService;

    @GetMapping("/send-otp")
    public String getOTPGet(Model model){
        model.addAttribute("request",new OtpLoginRequestDto());
        return "public/sendOTP";
    }

    @PostMapping("/send-otp")
    public String sendOTP(@Valid @ModelAttribute("request") OtpLoginRequestDto request, BindingResult result, Model model, HttpSession session){
        Customer customer=null;
        try{
            if(result.hasErrors()){
                model.addAttribute("request",request);
                session.setAttribute("message",new Message("Enter Valid NUmber","alert-danger"));
                return "public/sendOTP";
            }
          customer=customerService.getCustomerByMobile(request.getUsername());
            System.out.println(customer);

                String otp = twilioOtpService.sendOTP();
                logger.info(otp);
                customer.setOtp(bCryptPasswordEncoder.encode(otp));
            customerService.saveCustomer(customer);
//            twilioOtpService.deleteOTP();
            session.setAttribute("request",request);
            return "redirect:/user/login";
        }catch (NullPointerException ne){
            ne.printStackTrace();
            Customer newCustomer=new Customer();
            newCustomer.setMobileNumber(request.getUsername());
            newCustomer.setRole("ROLE_USER");
            String otp = twilioOtpService.sendOTP();
            logger.info(otp);
            newCustomer.setOtp(bCryptPasswordEncoder.encode(otp));
            customerService.saveCustomer(newCustomer);
            model.addAttribute("request",new OtpLoginRequestDto());
            return "redirect:/user/login";

        }
        catch(Exception e){
            e.printStackTrace();
            model.addAttribute("request",request);
                return "public/sendOTP";
        }




    }



}
