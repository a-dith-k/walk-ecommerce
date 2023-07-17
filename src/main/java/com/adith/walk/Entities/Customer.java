package com.adith.walk.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer userId;

    @Column(unique = true, nullable = false)
    private String mobileNumber;

    private String password;

    private String email;

    private String firstname;

    private String lastname;

    private String gender;

    private String role;

    private String accountStatus;

    private String otp;

    @Column(length = 50)
    private Date otpSendTime;

    @OneToMany
    private List<Wishlist> wishlist;


}
