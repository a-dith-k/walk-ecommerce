package com.adith.walk.helper;


import com.adith.walk.Entities.Customer;
import com.adith.walk.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClearOTPScheduler {


    private Customer customer;
    @Autowired
    CustomerService customerService;

//    public void deleteOTP(Customer customer){
//        this.customer=customer;
//        deleteOTP();
//    }
//
//
//
//    @Scheduled(initialDelay = 60000,fixedRate= 5*60000)
//    public void deleteOTP(){
//        customer.setOtp(null);
//        customerService.saveCustomer(customer);
//    }

}
