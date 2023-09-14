package com.adith.walk.service;

import com.adith.walk.Entities.Customer;
import com.adith.walk.Entities.Product;
import com.adith.walk.Entities.Wishlist;
import com.adith.walk.repositories.CustomerRepository;
import com.adith.walk.repositories.WishlistRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistService {


    final WishlistRepository wishlistRepository;
    final CustomerRepository customerRepository;

    final ProductService productService;


    public WishlistService(WishlistRepository wishlistRepository, CustomerRepository customerRepository, ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.customerRepository = customerRepository;
        this.productService = productService;
    }

    public Wishlist getWishlist(Customer customer) {

     return    wishlistRepository.findWishlistByCustomer(customer);
    }

    public void toggleToWishlist(Integer productId, Principal principal) {

        Product product = productService.getProductById(productId);
        Wishlist wishlistByCustomer = wishlistRepository.findWishlistByCustomer(customerRepository.findCustomerByMobileNumber(principal.getName()));

        if(wishlistByCustomer==null){
            wishlistByCustomer=new Wishlist();
            List<Product>pList=new ArrayList<>();
            wishlistByCustomer.setProducts(pList);
            wishlistByCustomer.setCustomer(customerRepository.findCustomerByMobileNumber(principal.getName()));
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
}
