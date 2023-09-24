package com.adith.walk.repositories;


import com.adith.walk.Entities.Product;
import com.adith.walk.Entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  StockRepository extends JpaRepository<Stock,Long> {


//    @Transactional
//    @Query("SELECT sum(size_2+size_3+size_4+size_5+size_6+size_7+size_8+size_9+size_10+size_11+size_12) from Stock  where  Id =?1")
//    Integer findStock(Integer id);
//

    Stock findStockByProduct(Product product);




}
