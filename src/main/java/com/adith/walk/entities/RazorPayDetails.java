package com.adith.walk.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RazorPayDetails {


    @Id
    private String paymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
    private Float amount;
    private Integer contactCount;

    private String companyName;
    private String currency;

    @OneToOne
    Orders order;


}
