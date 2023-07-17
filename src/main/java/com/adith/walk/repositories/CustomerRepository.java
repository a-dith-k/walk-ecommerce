package com.adith.walk.repositories;


import com.adith.walk.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {

        Customer findCustomerByEmail(String email);

        Customer findCustomerByMobileNumber(String mobile);

        Customer findCustomerByOtp(String otp);

        List<Customer> findCustomerByUserIdBetween(Integer start,Integer end);

        List<Customer> findFirstByFirstnameOrderByFirstname(String limit);


}
