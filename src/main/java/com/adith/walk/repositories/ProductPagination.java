package com.adith.walk.repositories;

import com.adith.walk.Entities.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPagination extends PagingAndSortingRepository<Product,Integer> {

}
