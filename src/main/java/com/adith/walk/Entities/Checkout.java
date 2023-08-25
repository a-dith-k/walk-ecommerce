package com.adith.walk.Entities;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class Checkout {


    Map<Product,Integer>products=new HashMap<>();

    Integer totalPrice;

    Address address;

    Customer customer;

    Payment payment;


}
