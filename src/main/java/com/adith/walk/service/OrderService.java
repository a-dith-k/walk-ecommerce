package com.adith.walk.service;

import com.adith.walk.Entities.Cart;
import com.adith.walk.Entities.Orders;
import com.adith.walk.Entities.Payment;
import com.adith.walk.enums.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
 public interface OrderService {

    void  placeOrder(Orders orders, Payment payment, Principal principal);


    List<Orders> getAllOrderByPrincipal(Principal principal);

    List<Orders> getAllOrders();

    Orders getOrderById(Long orderId);

    void updateOrderStatus(Long orderId, OrderStatus orderStatus);

    String getOrderStatus(Long orderId);

    Orders getCurrentOrder(Principal principal);

   Orders createOrder(Cart cartByPrincipal, Principal principal, HttpSession session);

    Orders buyNow(Integer productId,Principal principal,HttpSession session);


    Boolean isProductExistsInOrder(Principal principal, Integer productId);

    void cancelOrder(Long OrderId);

    void returnOrder(Long orderId);
}
