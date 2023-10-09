package com.adith.walk.service.address.service;

import com.adith.walk.dto.AddressRequest;
import com.adith.walk.entities.Address;
import com.adith.walk.exceptions.AddressNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface AddressService {


    List<Address> getAllAddress(Principal principal);

    Address addressById(Long addressId);

    Address save(AddressRequest addressRequest, Principal principal) throws AddressNotFoundException;

    void delete(Long addressId);

    void update(AddressRequest addressUpdate) throws AddressNotFoundException;

    Address getDefaultAddress(Principal principal);

    void setDefault(Long addressId, Principal principal) throws AddressNotFoundException;

    Address getBillingAddress(Principal principal);

    void setBillingAddress(Long addressId, Principal principal);

    void updateOrderDeliveryAddress(Long addressId, HttpSession session);
}
