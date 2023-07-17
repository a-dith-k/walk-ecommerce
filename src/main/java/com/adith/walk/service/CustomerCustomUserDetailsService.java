package com.adith.walk.service;

import com.adith.walk.Entities.Customer;
import com.adith.walk.Entities.CustomerUserDetails;
import com.adith.walk.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomerCustomUserDetailsService implements UserDetailsService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer= customerRepository.findCustomerByMobileNumber(username);

        if(customer==null){
            throw new UsernameNotFoundException("User could not be found");
        }

        return new CustomerUserDetails(customer);
    }


    }

