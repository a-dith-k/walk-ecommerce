package com.adith.walk.service.stock.service;

import com.adith.walk.entities.Stock;
import com.adith.walk.repositories.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImplementation implements StockService {


    final StockRepository stockRepository;


    public StockServiceImplementation(StockRepository stockRepository) {
        this.stockRepository = stockRepository;

    }

    @Override
    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    @Override
    public void save(Stock stock) {
        stockRepository.save(stock);
    }


    @Override
    public Stock getStockById(long stockId) {
        return stockRepository.findById(stockId).orElse(new Stock());
    }

    @Override
    public Long getStockOutProductCount() {
        return stockRepository.findStockOutProductCount();
    }


}
