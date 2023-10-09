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

    private Integer quantity;

    private Long totalPrice;

    private Long discountedPrice;

    private Long totalMRP;

    private Long totalDiscount;

    private OrderStatus orderStatus;

    Boolean isPaymentDone;

}
