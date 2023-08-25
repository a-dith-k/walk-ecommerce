package com.adith.walk.service;

import org.springframework.stereotype.Service;

@Service
public interface CartItemService {

    void addQuantity(Long id);
    void removeQuantity(Long id);
}
