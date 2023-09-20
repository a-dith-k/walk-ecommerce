package com.adith.walk.repositories;


import com.adith.walk.Entities.Address;
import com.adith.walk.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public
interface AddressRepository extends JpaRepository<Address,Long> {

    List<Address> findAddressesByCustomer(Customer customer);


    Address findByIsBillingTrueAndCustomer(Customer customer);


}
