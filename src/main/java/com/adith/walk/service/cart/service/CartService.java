package com.adith.walk.service.cart.service;

import com.adith.walk.entities.Cart;
import com.adith.walk.entities.Product;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface CartService {

    void add(Integer productId, long sizeId, Principal principal) throws AlreadyUsedException;

    Cart getCartByCustomerId(Long customerId);

    Boolean isProductAlreadyExistsInCart(Principal principal, Product product);

    void deleteProduct(Integer productId, Principal principal);


    Cart getCartByPrincipal(Principal principal);

    void deleteItem(Long itemId);

    Boolean isCartEmpty(Principal principal);

    void deleteCart(Principal principal);
}
