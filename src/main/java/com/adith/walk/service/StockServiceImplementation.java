package com.adith.walk.service;

import com.adith.walk.Entities.Product;
import com.adith.walk.Entities.Stock;
import com.adith.walk.repositories.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImplementation implements StockService{


    final StockRepository stockRepository;

    public StockServiceImplementation(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> getAllStock() {
       return stockRepository.findAll();
    }

    @Override
    public void addStock(Product product, Long quantity, String size) {
//        Stock stock = stockRepository.findStockByProduct(product);
//
//        stock.

    }

    @Override
    public void removeStock(Product product, Long quantity, String size) {

    }

    @Override
    public Stock save(Stock stock) {
           return stockRepository.save(stock);
    }

    @Override
    public Stock getStockByProduct(Product product) {
      return stockRepository.findStockByProduct(product);
    }
}
