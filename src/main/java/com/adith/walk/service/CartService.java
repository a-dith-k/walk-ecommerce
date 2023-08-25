package com.adith.walk.service;

import com.adith.walk.Entities.Cart;
import com.adith.walk.Entities.Customer;
import com.adith.walk.Entities.Product;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface CartService {

    void add(Integer productId, Principal principal) throws AlreadyUsedException;

    Cart getCartByCustomerId(Long customerId);

    Boolean isProductAlreadyExistsInCart(Principal principal,Product product);

    void deleteProduct(Integer productId, Principal principal);


    public Cart getCartByPrincipal(Principal principal);

    void deleteItem(Long itemId);

    Boolean isCartEmpty(Principal principal);

    void deleteCart(Principal principal);
}
