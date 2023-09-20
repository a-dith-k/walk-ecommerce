package com.adith.walk.service;

import com.adith.walk.Entities.Address;
import com.adith.walk.Entities.Orders;
import com.adith.walk.dto.AddressRequest;
import com.adith.walk.repositories.AddressRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
public class AddressService {

    AddressRepository addressRepository;

    CustomerService customerService;

    ModelMapper mapper;



    public List<Address> getAllAddress(Principal principal){

        return addressRepository
                .findAddressesByCustomer(customerService
                        .getCustomerByMobile(principal.getName()));

    }


    public Address addressById(Long addressId) {

      return  addressRepository.findById(addressId).get();

    }

    public Address save(AddressRequest addressRequest, Principal principal) {

        Address address=new Address();
        mapper.map(addressRequest,address);


        address.setCustomer( customerService
                        .getCustomerByMobile(principal.getName()));
        Address save = addressRepository
                .save(address);
        setDefault(save.getId(),principal);
        return save;
    }

    public void delete(Long addressId) {

        addressRepository.deleteById(addressId);
    }

    public void update(AddressRequest addressUpdate) {

       Address address=addressRepository.findById(addressUpdate.getId()).get();
         mapper.map(addressUpdate,address);


         addressRepository.save(address);

    }

    public Address getDefaultAddress(Principal principal) {

      return   getAllAddress(principal)
              .stream()
              .filter(address->address
                      .getIsDefault())
              .findAny()
              .orElse(null);
    }

    public void setDefault(Long addressId,Principal principal) {

        List<Address> allAddress = getAllAddress(principal);
        allAddress.forEach(address ->{
            address.setIsDefault(false);
            addressRepository.save(address);
        } );
        Address address = addressRepository.findById(addressId).get();
        address.setIsDefault(true);
        addressRepository.save(address);

    }

    public Address getBillingAddress(Principal principal) {

      return   addressRepository.findByIsBillingTrueAndCustomer(customerService.getCustomerByMobile(principal.getName()));

    }

    public boolean setBillingAddress(Long addressId, Principal principal) {

        List<Address> addressesByCustomer = addressRepository.findAddressesByCustomer(customerService.getCustomerByMobile(principal.getName()));

        addressesByCustomer.forEach(address -> {
            address.setIsBilling(false);
            addressRepository.save(address);
        });
        addressesByCustomer.stream().filter(address -> address.getId().equals(addressId)).forEach(address -> {
            address.setIsBilling(true);
            addressRepository.save(address);
        });

        return true;
    }

    public void updateOrderDeliveryAddress(Long addressId, HttpSession session) {

        Orders order =(Orders) session.getAttribute("order");

        order.setDeliveryAddress(addressRepository.findById(addressId).orElse(order.getDeliveryAddress()));


    }
}
