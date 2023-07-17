package com.adith.walk.Entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;

public class CustomAdminDetails implements UserDetails {

    private Admin admin;
    public CustomAdminDetails(Admin admin) {
        this.admin=admin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

//        return Collections.singleton(new SimpleGrantedAuthority(admin.getRole()));

        SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(admin.getRole());
        return List.of(simpleGrantedAuthority);
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getUsername();
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
