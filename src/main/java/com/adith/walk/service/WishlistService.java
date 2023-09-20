package com.adith.walk.service;

import com.adith.walk.Entities.Customer;
import com.adith.walk.Entities.Product;
import com.adith.walk.Entities.Wishlist;
import com.adith.walk.repositories.WishlistRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistService {


    final WishlistRepository wishlistRepository;
    final CustomerService customerService;

    final ProductService productService;


    public WishlistService(WishlistRepository wishlistRepository, CustomerService customerService, ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.customerService = customerService;
        this.productService = productService;
    }

    public Wishlist getWishlist(Customer customer) {

     return    wishlistRepository.findWishlistByCustomer(customer);
    }

    public void toggleToWishlist(Integer productId, Principal principal) {

        Product product = productService.getProductById(productId);
        Wishlist wishlistByCustomer = wishlistRepository.findWishlistByCustomer(customerService.getCustomerByMobile(principal.getName()));

        if(wishlistByCustomer==null){
            wishlistByCustomer=new Wishlist();
            List<Product>pList=new ArrayList<>();
            wishlistByCustomer.setProducts(pList);
            wishlistByCustomer.setCustomer(customerService.getCustomerByMobile(principal.getName()));
        }


        boolean productExists = wishlistByCustomer.getProducts().stream().anyMatch(p -> p==product);
        System.out.println(productExists);

        if(productExists){
            wishlistByCustomer.getProducts().remove(product);
        }else{
            wishlistByCustomer.getProducts().add(product);
            System.out.println("Called-------------------------------------------------------");
        }

        wishlistRepository.save(wishlistByCustomer);
    }

    public boolean isProductWishlist(Product product,Principal principal) {


        Wishlist wishlist =getWishlist(customerService.getCustomerByMobile(principal.getName()));
        if(wishlist==null){
            return false;
        }

        return  wishlist.getProducts().stream().anyMatch(product1 -> product1.equals(product));

    }
}
