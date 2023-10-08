package com.adith.walk.service.stock.service;


import com.adith.walk.entities.Product;
import com.adith.walk.entities.Stock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StockService {
    List<Stock> getAllStock();

    void addStock(Product product, Long quantity, String size);

    void removeStock(Product product, Long quantity, String size);

    Stock save(Stock stock);

    Stock getStockByProduct(Product product);

    Stock getStockById(long stockId);

    Long getStockOutProductCount();
}
