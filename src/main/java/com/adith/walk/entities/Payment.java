package com.adith.walk.entities;

import com.adith.walk.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
public class Payment implements Serializable {

    @SequenceGenerator(name = "paymentIdGenerator",
            sequenceName = "paymentIdGenerator",
            initialValue = 1000001, allocationSize = 1)

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paymentIdGenerator")
    Long paymentId;

    Long totalAmount;

    String method;

    PaymentStatus paymentStatus;

    LocalDateTime dateTime;


    @JsonBackReference
    @OneToOne(mappedBy = "payment")
    Orders orders;
}
