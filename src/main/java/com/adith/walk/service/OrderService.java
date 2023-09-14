package com.adith.walk.service;

import com.adith.walk.Entities.Cart;
import com.adith.walk.Entities.CustomerOrder;
import com.adith.walk.Entities.Payment;
import com.adith.walk.enums.OrderStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
 public interface OrderService {

    boolean placeOrder(Cart cart, Payment payment, Principal principal);


    List<CustomerOrder> getAllOrderByPrincipal(Principal principal);

    List<CustomerOrder> getAllOrders();

    CustomerOrder getOrderById(Long orderId);

    void updateOrderStatus(Long orderId, OrderStatus orderStatus);

    String getOrderStatus(Long orderId);
}
