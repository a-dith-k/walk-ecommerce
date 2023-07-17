package com.adith.walk.service;


import com.adith.walk.Entities.Customer;
import com.adith.walk.repositories.CustomerPagination;
import com.adith.walk.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerPagination customerPagination;

    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public Customer getCustomerByMobile(String mobile) {

        return customerRepository.findCustomerByMobileNumber(mobile);

    }

    public List<Customer> getCustomerBetween(Integer start, Integer end){

        return customerRepository.findCustomerByUserIdBetween(start,end);
    }


    public List<Customer>getPageOfCustomer(Integer page,Integer count){
        Page<Customer> all = customerPagination.findAll(PageRequest.of(page, count));
        List<Customer>CustomerPage=new ArrayList<>();
        all.stream().forEach(customer -> CustomerPage.add(customer));
       return CustomerPage;
    }

    public void deleteOTPByOTP(String otp) {

            Customer customerByOtp = customerRepository.findCustomerByOtp(otp);
            customerByOtp.setOtp(null);
            customerRepository.save(customerByOtp);
    }




}
