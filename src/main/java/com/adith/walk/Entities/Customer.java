package com.adith.walk.Entities;

import com.adith.walk.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.antlr.v4.runtime.Token;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

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


    private String firstName;

    private String lastName;

    private String gender;

    @DateTimeFormat
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean enabled=false;

    private boolean locked= false;

    @OneToOne
    private Wishlist wishlist;

    @OneToOne(cascade=CascadeType.ALL)
    ConfirmToken confirmToken;


    @OneToMany(mappedBy = "customer" ,cascade = CascadeType.ALL)
    List<Address>addresses=new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL)
    private Cart cart;


}
