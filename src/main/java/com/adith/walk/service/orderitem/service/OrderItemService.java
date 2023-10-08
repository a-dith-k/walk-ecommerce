package com.adith.walk.service.orderitem.service;

import com.adith.walk.entities.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderItemService {

    List<OrderItem> getAllOrderItems();
}
