package com.adith.walk.entities;

import com.adith.walk.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {

    @SequenceGenerator(
            name = "customer_sequence",
            sequenceName = "customer_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_sequence"
    )
    private Long userId;

    @Column(unique = true, nullable = false)
    private String mobileNumber;

    @Column(unique = true, nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    private String referralEmail;


    private String firstName;

    private String lastName;

    private String gender;

    @DateTimeFormat
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean enabled = false;

    private boolean locked = false;

    @OneToOne(mappedBy = "customer")
    private Wishlist wishlist;

    @OneToOne(cascade = CascadeType.ALL)
    ConfirmToken confirmToken;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<Address> addresses = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    private Cart cart;

    @OneToMany(mappedBy = "customer")
    List<Orders> orders = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    List<Coupon> coupons = new ArrayList<>();


}
