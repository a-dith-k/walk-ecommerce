package com.adith.walk.service.order.service;

import com.adith.walk.entities.Cart;
import com.adith.walk.entities.Orders;
import com.adith.walk.entities.Payment;
import com.adith.walk.enums.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface OrderService {

    Orders placeOrder(Orders orders, Payment payment, Principal principal);


    List<Orders> getAllOrderByPrincipal(Principal principal);

    List<Orders> getAllOrders();

    Orders getOrderById(Long orderId);

    void updateOrderStatus(Long orderId, OrderStatus orderStatus);

    String getOrderStatus(Long orderId);


    void createOrder(Cart cartByPrincipal, Principal principal, HttpSession session);

    void buyNow(Integer productId, Long sizeId, Principal principal, HttpSession session);


    Boolean isProductExistsInOrder(Principal principal, Integer productId);

    void cancelOrder(Long OrderId);

    void returnOrder(Long orderId);

    void save(Orders order);

    Long getSaleToday();

    Long getTotalOrderToday();
}
