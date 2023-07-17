package com.adith.walk.service;

import com.adith.walk.Entities.Admin;
import com.adith.walk.Entities.CustomAdminDetails;
import com.adith.walk.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminCustomUserDetailsService implements UserDetailsService {

    @Autowired
    AdminRepository adminRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Admin admin=adminRepository.findAdminByUsername(username);
        System.out.println("i readch");
        System.out.println("ADMIN"+admin);

        if(admin==null){
            throw new UsernameNotFoundException("admin could be found");
        }
            return new CustomAdminDetails(admin);
    }
}
