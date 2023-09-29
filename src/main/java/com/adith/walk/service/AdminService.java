package com.adith.walk.service;


import com.adith.walk.entities.Admin;
import com.adith.walk.entities.Customer;
import com.adith.walk.repositories.AdminRepository;
import com.adith.walk.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AdminRepository adminRepository;

    public List<Customer> getAllUsers() {

        return customerRepository.findAll();
    }

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }
}
