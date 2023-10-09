package com.adith.walk.service.stock.service;


import com.adith.walk.entities.Stock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StockService {
    List<Stock> getAllStock();


    void save(Stock stock);

    Stock getStockById(long stockId);

    Long getStockOutProductCount();
}
