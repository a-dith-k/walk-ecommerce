package com.adith.walk.service.cart.service;

import com.adith.walk.entities.Cart;
import com.adith.walk.entities.CartItem;
import com.adith.walk.entities.Customer;
import com.adith.walk.entities.Product;
import com.adith.walk.repositories.CartItemRepository;
import com.adith.walk.repositories.CartRepo;
import com.adith.walk.service.CustomerService;
import com.adith.walk.service.ProductService;
import com.adith.walk.service.SizeService;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    CartRepo cartRepo;
    CustomerService customerService;
    ProductService productService;

    CartItemRepository cartItemRepository;

    SizeService sizeService;


    @Override
    public void add(Integer productId, long sizeId, Principal principal) throws AlreadyUsedException {

        //getting current logged in customer
        Customer customer =
                customerService
                        .getCustomerByMobile(
                                principal.getName());

        Product product =
                productService
                        .getProductById(productId);


        Cart cart = new Cart();
        //getting cart of the current customer if exists
        if (getCartByCustomerId(customer.getUserId()) != null) {
            cart = getCartByCustomerId(customer.getUserId());
        }


        List<CartItem> items = cart.getItems();

        for (CartItem item : items) {
            if (item.getProduct().equals(product)) {
                throw new AlreadyUsedException("product Already Added");
            }

        }

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1L);
        cartItem.setTotalPrice(product.getOfferPrice());
        cartItem.setTotalMRP(product.getProductMrp());
        cartItem.setProductSize(sizeService.getSizeBySizeId(sizeId));
        cartItem.setTaxAmount(productService
                .getProductTax(product.getOfferPrice(), product.getTaxRate().intValue()));

        items.add(cartItem);

        cartItem.setCart(cart);
        customer.setCart(cart);

        if (cart.getTotalPrice() == null || cart.getTotalPrice() <= 0) {
            cart.setTotalPrice(cartItem.getTotalPrice());
            cart.setTotalMRP(cartItem.getTotalMRP());
            cart.setTotalDiscount(cart.getTotalMRP() - cart.getTotalPrice());
            cart.setQuantity(1);
        } else {
            cart.setTotalPrice(cart.getTotalPrice() + cartItem.getTotalPrice());
            cart.setTotalMRP(cart.getTotalMRP() + cartItem.getTotalMRP());
            cart.setTotalDiscount(cart.getTotalMRP() - cart.getTotalPrice());
            cart.setQuantity(cart.getQuantity() + 1);
        }


        cartRepo.save(cart);
    }

    @Override
    public Cart getCartByCustomerId(Long customerId) {


        return cartRepo.findCartByCustomer(customerService.getCustomerById(customerId));

    }

    @Override
    public Boolean isProductAlreadyExistsInCart(Principal principal, Product product) {

        Cart cart = getCartByCustomerId(customerService.getCustomerByMobile(principal.getName()).getUserId());

        List<CartItem> items = cart.getItems();

        for (CartItem item : items) {
            if (item.getProduct().equals(product)) {
                return true;
            }

        }

        return false;
    }

    @Override
    public void deleteProduct(Integer productId, Principal principal) {
//        Cart cart = getCartByPrincipal(principal);
//        for(CartItem item :cart.getItems()){
//            if(item.getProduct().getProductId().equals(productId)){
//
//                cartItemRepository.delete(item);
//            }
//        }
    }

    @Override
    public Cart getCartByPrincipal(Principal principal) {

        return getCartByCustomerId(customerService.getCustomerByMobile(principal.getName()).getUserId());
    }

    @Override
    public void deleteItem(Long itemId) {

        CartItem cartItem = cartItemRepository.findById(itemId).get();
        Cart cart = cartItem.getCart();
        if (cart.getQuantity() != null && cart.getQuantity() > 0) {
            cart.setTotalMRP(cart.getTotalMRP() - cartItem.getProduct().getProductMrp());
            cart.setTotalPrice(cart.getTotalPrice() - cartItem.getProduct().getOfferPrice());
            cart.setTotalDiscount(cart.getTotalMRP() - cart.getTotalPrice());
            cart.setQuantity(cart.getQuantity() - 1);
        }


        cartItemRepository.delete(cartItem);
    }

    @Override
    public Boolean isCartEmpty(Principal principal) {

        return true;
    }

    @Override
    public void deleteCart(Principal principal) {

        Cart cart = getCartByPrincipal(principal);

        Customer customer = customerService.getCustomerByMobile(principal.getName());
        Cart newCart = new Cart().builder().quantity(0).totalDiscount(0L).totalPrice(0L).totalMRP(0L).build();
        customer.setCart(newCart);
        customerService.saveCustomer(customer);


//        cart.getItems().forEach(product->cartItemRepository.delete(product));
//        cart.setItems(null);
//
//        cart.setTotalPrice(0l);
//        cart.setTotalMRP(0l);
//        cart.setTotalDiscount(0l);
//        cartRepo.save(cart);


    }


}
