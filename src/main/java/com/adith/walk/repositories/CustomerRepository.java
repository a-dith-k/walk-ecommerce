package com.adith.walk.repositories;


import com.adith.walk.entities.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findCustomerByUserId(Long id);

    Customer findCustomerByEmail(String email);

    Customer findCustomerByMobileNumber(String mobile);


    @Transactional
    @Modifying
    @Query("UPDATE Customer c " +
            "SET c.enabled = TRUE WHERE c.mobileNumber = ?1")
    void enableCustomer(String mobile);


    @Transactional
    @Modifying
    @Query("UPDATE Customer c " +
            "SET c.enabled = FALSE WHERE c.userId = ?1")
    void disableCustomer(Integer userId);


}
