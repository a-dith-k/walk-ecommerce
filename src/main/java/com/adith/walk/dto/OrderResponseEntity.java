package com.adith.walk.dto;

import com.adith.walk.entities.Customer;
import com.adith.walk.entities.OrderItem;
import com.adith.walk.enums.OrderStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderResponseEntity {

    private Long id;


    private List<OrderItem> items = new ArrayList<>();


    private Customer customer;


//    private Address billingAddress;
//
//
//    private Address deliveryAddress;


//    private Payment payment;

//    RazorPayDetails razorPayDetails;

    private Integer quantity;

    private Long totalPrice;

    private Long discountedPrice;

    private Long totalMRP;

    private Long totalDiscount;

    private OrderStatus orderStatus;


//    private OrderHistory orderHistory;


    Boolean isPaymentDone;
//
//    private Coupon coupon;
}
