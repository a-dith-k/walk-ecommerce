package com.adith.walk.service;


import com.adith.walk.Entities.Checkout;
import com.adith.walk.Entities.Customer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CheckoutService {

    Map<Customer, Checkout>checkoutData=new HashMap<>();
}
