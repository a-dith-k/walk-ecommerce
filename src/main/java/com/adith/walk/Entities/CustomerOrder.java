package com.adith.walk.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class CustomerOrder {

    @SequenceGenerator(name = "orderIdGenerator",
    sequenceName = "orderIdGenerator",
    allocationSize = 1,
    initialValue = 60000001)

    @Id
            @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "orderIdGenerator")
    Long orderId;

    @OneToOne()
    Cart cart;

    @ManyToOne
    Address billingAddress;

    @ManyToOne
    Address deliveryAddress;

    @OneToOne
    Payment payment;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "order")
    List<CustomerOrderStatus> orderStatus=new ArrayList<>();


    @ManyToOne
    Customer customer;


}
