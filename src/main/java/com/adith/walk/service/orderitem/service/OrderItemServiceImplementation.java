package com.adith.walk.service.orderitem.service;


import com.adith.walk.entities.OrderItem;
import com.adith.walk.repositories.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImplementation implements OrderItemService {

    final OrderItemRepository orderItemRepository;

    public OrderItemServiceImplementation(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderItem> getAllOrderItems() {

        return orderItemRepository.findAll();
    }
}
