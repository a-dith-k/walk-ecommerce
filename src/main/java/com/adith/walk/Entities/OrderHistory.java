package com.adith.walk.Entities;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
public class OrderHistory implements Serializable {


    @SequenceGenerator(name = "orderHistoryIdGenerator",
    sequenceName = "orderHistoryIdGenerator",
    allocationSize = 1)

    @Id
            @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "orderHistoryIdGenerator" )
    Long id;

    private LocalDateTime orderTime;
    private LocalDateTime shippingTime;
    private LocalDateTime outForDeliveryTime;
    private LocalDateTime DeliveredTime;


}
