package com.adith.walk.service.address.service;

import com.adith.walk.dto.AddressRequest;
import com.adith.walk.entities.Address;
import com.adith.walk.entities.Orders;
import com.adith.walk.exceptions.AddressNotFoundException;
import com.adith.walk.repositories.AddressRepository;
import com.adith.walk.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service

public class AddressServiceImpl implements AddressService {

    final AddressRepository addressRepository;

    final CustomerService customerService;

    final ModelMapper mapper;

    public AddressServiceImpl(AddressRepository addressRepository, CustomerService customerService, ModelMapper mapper) {
        this.addressRepository = addressRepository;
        this.customerService = customerService;
        this.mapper = mapper;
    }

    @Override
    public List<Address> getAllAddress(Principal principal) {

        return addressRepository
                .findAddressesByCustomer(customerService
                        .getCustomerByMobile(principal.getName()));

    }

    @Override
    public Address addressById(Long addressId) {

        return addressRepository.findById(addressId).orElse(new Address());

    }

    @Override
    public Address save(AddressRequest addressRequest, Principal principal) throws AddressNotFoundException {

        Address address = new Address();
        mapper.map(addressRequest, address);


        address.setCustomer(customerService
                .getCustomerByMobile(principal.getName()));
        Address save = addressRepository
                .save(address);
        setDefault(save.getId(), principal);
        return save;
    }

    @Override
    public void delete(Long addressId) {

        addressRepository.deleteById(addressId);
    }

    @Override
    public void update(AddressRequest addressUpdate) throws AddressNotFoundException {

        Address address = addressRepository.findById(addressUpdate.getId()).orElseThrow(() -> new AddressNotFoundException("Address not found"));
        mapper.map(addressUpdate, address);


        addressRepository.save(address);

    }

    @Override
    public Address getDefaultAddress(Principal principal) {

        return getAllAddress(principal)
                .stream()
                .filter(Address::getIsDefault)
                .findAny()
                .orElse(null);
    }

    @Override
    public void setDefault(Long addressId, Principal principal) throws AddressNotFoundException {

        List<Address> allAddress = getAllAddress(principal);
        allAddress.forEach(address -> {
            address.setIsDefault(false);
            addressRepository.save(address);
        });
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException("Address not found"));
        address.setIsDefault(true);
        addressRepository.save(address);

    }

    @Override
    public Address getBillingAddress(Principal principal) {

        return addressRepository.findByIsBillingTrueAndCustomer(customerService.getCustomerByMobile(principal.getName()));

    }

    @Override
    public void setBillingAddress(Long addressId, Principal principal) {

        List<Address> addressesByCustomer = addressRepository.findAddressesByCustomer(customerService.getCustomerByMobile(principal.getName()));

        addressesByCustomer.forEach(address -> {
            address.setIsBilling(false);
            addressRepository.save(address);
        });
        addressesByCustomer.stream().filter(address -> address.getId().equals(addressId)).forEach(address -> {
            address.setIsBilling(true);
            addressRepository.save(address);
        });

    }

    @Override
    public void updateOrderDeliveryAddress(Long addressId, HttpSession session) {

        Orders order = (Orders) session.getAttribute("order");

        order.setDeliveryAddress(addressRepository.findById(addressId).orElse(order.getDeliveryAddress()));


    }
}
