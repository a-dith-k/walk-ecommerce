package com.adith.walk.controllers;


import com.adith.walk.Entities.Product;
import com.adith.walk.dto.ProductPageDTO;
import com.adith.walk.repositories.StockRepository;
import com.adith.walk.service.CartService;
import com.adith.walk.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
@Controller
@AllArgsConstructor
@RequestMapping("products")
public class ProductController {

    ProductService productService;

    StockRepository stockRepository;

    CartService cartService;

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
    public String  getProduct(@PathVariable Integer id, Model model, Principal principal){

        Product productById = productService.getProductById(id);

        Integer stock = stockRepository.findStock(5001);
        Integer stock2 = stockRepository.findStock(productById.getStock().getId());
        if(principal!=null){
            model.addAttribute("isProductExists",cartService.isProductAlreadyExistsInCart(principal,productById));
        }else{
            model.addAttribute("isProductExists",false);
        }

        model.addAttribute("product",productById);

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




}
