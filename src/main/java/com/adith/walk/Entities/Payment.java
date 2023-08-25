package com.adith.walk.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Payment {

    @SequenceGenerator(name = "paymentIdGenerator",
    sequenceName = "paymentIdGenerator",
    initialValue = 1000001,allocationSize = 1)

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "paymentIdGenerator")
    Long paymentId;

    Long totalAmount;

    String method;

    String status;

    LocalDateTime dateTime;
}
