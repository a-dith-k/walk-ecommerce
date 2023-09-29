package com.adith.walk.service;


import com.adith.walk.entities.Cart;
import com.adith.walk.entities.CartItem;
import com.adith.walk.repositories.CartItemRepository;
import com.adith.walk.service.cart.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartItemServiceImplementation implements CartItemService {


    CartItemRepository cartItemRepository;

    CartService cartService;


    @Override
    public void addQuantity(Long id) {
        CartItem cartItem = cartItemRepository.findById(id).get();

        Cart cart = cartItem.getCart();

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItem.setTotalMRP(cartItem.getTotalMRP() + cartItem.getProduct().getProductMrp());
        cartItem.setTotalPrice(cartItem.getTotalPrice() + cartItem.getProduct().getOfferPrice());

        cart.setQuantity(cart.getQuantity() + 1);
        cart.setTotalPrice(cart.getTotalPrice() + cartItem.getProduct().getOfferPrice());
        cart.setTotalMRP(cart.getTotalMRP() + cartItem.getProduct().getProductMrp());
        cart.setTotalDiscount(cart.getTotalMRP() - cart.getTotalPrice());

        cartItemRepository.save(cartItem);

    }

    @Override
    public void removeQuantity(Long id) {

        CartItem cartItem = cartItemRepository.findById(id).get();


        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItem.setTotalPrice(cartItem.getTotalPrice() - cartItem.getProduct().getOfferPrice());
            cartItem.setTotalMRP(cartItem.getTotalMRP() - cartItem.getProduct().getProductMrp());


            Cart cart = cartItem.getCart();

            cart.setQuantity(cart.getQuantity() - 1);
            cart.setTotalMRP(cart.getTotalMRP() - cartItem.getProduct().getProductMrp());
            cart.setTotalPrice(cart.getTotalPrice() - cartItem.getProduct().getOfferPrice());
            cart.setTotalDiscount(cart.getTotalMRP() - cart.getTotalPrice());
//            cartService.save(cart);

            cartItemRepository.save(cartItem);
        } else {
            cartService.deleteItem(cartItem.getItemId());
        }


    }
}
