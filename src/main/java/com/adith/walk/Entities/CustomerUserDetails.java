package com.adith.walk.Entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomerUserDetails implements UserDetails {

    private Customer customer;
    public CustomerUserDetails(Customer customer) {
        this.customer=customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.singleton(new SimpleGrantedAuthority(customer.getRole()));
//        SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(customer.getRole());
//
//        return List.of(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return customer.getOtp().toString();
    }

    @Override
    public String getUsername() {
       return customer.getMobileNumber().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
