package com.adith.walk.service;


import com.adith.walk.Entities.Product;
import com.adith.walk.Entities.Stock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StockService {
    List<Stock> getAllStock();

    void addStock(Product product,Long quantity,String size);
    void removeStock(Product product,Long quantity,String size);
}
