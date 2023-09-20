package com.adith.walk.Entities;


import com.adith.walk.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Orders implements Serializable {

    @SequenceGenerator(name = "orderIdGenerator",
    sequenceName ="orderIdGenerator",
    allocationSize = 1)

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "orderIdGenerator")
    private Long id;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<OrderItem> items=new ArrayList<>();

    @ManyToOne()
    private Customer customer;

    @ManyToOne
    private Address billingAddress;

    @ManyToOne
    private Address deliveryAddress;

    @OneToOne
    private Payment payment;

    private Integer quantity;

    private Long totalPrice;

    private Long totalMRP;

    private Long totalDiscount;

    private OrderStatus orderStatus;

    @OneToOne(cascade = CascadeType.ALL)
    private OrderHistory orderHistory;

    boolean isCurrentOrder;

    Boolean isPaymentDone;


}
