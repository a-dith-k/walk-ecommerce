package com.adith.walk.repositories;

import com.adith.walk.Entities.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPagination extends PagingAndSortingRepository<Customer,Integer> {


}
