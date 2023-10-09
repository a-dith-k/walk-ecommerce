package com.adith.walk.repositories;


import com.adith.walk.entities.Product;
import com.adith.walk.entities.Stock;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {


    Stock findStockByProduct(Product product);


    @Transactional
    @Query("select count(*) from Stock  s where s.totalStock=0")
    Long findStockOutProductCount();
}
