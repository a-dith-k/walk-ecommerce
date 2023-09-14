package com.adith.walk.controllers;


import com.adith.walk.Entities.Product;
import com.adith.walk.Entities.ProductReview;
import com.adith.walk.dto.ProductPageDTO;
import com.adith.walk.repositories.StockRepository;
import com.adith.walk.service.CartService;
import com.adith.walk.service.CustomerService;
import com.adith.walk.service.ProductService;
import com.adith.walk.service.ReviewService;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@Controller

@RequestMapping("products")
public class ProductController {

    final ProductService productService;

    final StockRepository stockRepository;

    final CartService cartService;

    final ReviewService reviewService;

    final CustomerService customerService;

    public ProductController(ProductService productService, StockRepository stockRepository, CartService cartService, ReviewService reviewService, CustomerService customerService) {
        this.productService = productService;
        this.stockRepository = stockRepository;
        this.cartService = cartService;
        this.reviewService = reviewService;
        this.customerService = customerService;
    }

    //produces = MediaType.APPLICATION_JSON_VALUE

    @GetMapping()
    public String getProductPage( Model model){
        model.addAttribute("products",productService.getProductByPage(0,12));

        return "public/products";
    }

    @GetMapping("page/{pageNumber}")
    public String getProductPage(@PathVariable Integer pageNumber, Model model){
        model.addAttribute("products",productService.getProductByPage(pageNumber,12));

        return "public/products";
    }


    @GetMapping("{id}")
    public String  getProduct(@ModelAttribute("productReview") ProductReview productReview, @PathVariable Integer id, Model model, Principal principal){

        Product product = productService.getProductById(id);



//        Integer stock = stockRepository.findStock(5001);
//        Integer stock2 = stockRepository.findStock(productById.getStock().getId());
        if(principal!=null){
            model.addAttribute("isProductExists",cartService.isProductAlreadyExistsInCart(principal,product));
            model.addAttribute("activeCustomer",customerService.getCustomerByMobile(principal.getName()));
            model.addAttribute("in", customerService.isProductWishlist(product,principal));
        }else{
            model.addAttribute("isProductExists",false);
        }

        model.addAttribute("product",product);
        model.addAttribute("reviews",reviewService.getAllReviews(product));

        model.addAttribute("isUserLoggedIn", principal != null);





        return "public/product";
    }

    @GetMapping("men/{pageNumber}")
    public String  getProductCategoryMen(Model model, @PathVariable Integer pageNumber){
        ProductPageDTO productPageDTO = productService.getMenProducts(pageNumber, 2);
        model.addAttribute("productPageResponseDTO",productPageDTO);

        return "public/products";
    }

    @GetMapping("women/{pageNumber}")
    public String  getProductCategoryWomen(Model model, @PathVariable Integer pageNumber){

        ProductPageDTO productPageDTO = productService.getWomenProducts(pageNumber, 2);
        model.addAttribute("productPageResponseDTO",productPageDTO);

        return "public/products";
    }



    @PostMapping("{productId}/review/add")
    public String addReview(@ModelAttribute("productReview") ProductReview productReview, @PathVariable Integer productId, Principal principal){


        try {
            reviewService.addReview(productReview,productId,principal);
        } catch (AlreadyUsedException e) {
            return  "redirect:/products/"+productId;
        }

        return "redirect:/products/"+productId;

    }




}
