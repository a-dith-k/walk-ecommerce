package com.adith.walk.controllers;

import com.adith.walk.Entities.Product;
import com.adith.walk.dto.ProductPageDTO;
import com.adith.walk.dto.ProductSearchRequest;
import com.adith.walk.service.*;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    final
    PasswordEncoder passwordEncoder;
     final
    AdminService adminService;
    final
    CustomerService customerService;
    final
    ProductService productService;
    final
    BannerService bannerService;

    final ModelMapper modelMapper;

    final CartService cartService;

    public HomeController(PasswordEncoder passwordEncoder,
                          AdminService adminService,
                          CustomerService customerService,
                          ProductService productService,
                          BannerService bannerService,
                          ModelMapper modelMapper,CartService cartService) {

                                    this.passwordEncoder = passwordEncoder;
                                    this.adminService = adminService;
                                    this.customerService = customerService;
                                    this.productService = productService;
                                    this.bannerService = bannerService;
                                    this.modelMapper=modelMapper;
                                    this.cartService=cartService;
    }


//    @ApiIgnore
//    @RequestMapping(value="/swagger")
//    public void redirect(HttpServletResponse response) throws IOException {
//        response.sendRedirect("/swagger-ui.html");
//    }

    @GetMapping()
    public String  getHome(Principal principal, Model model, HttpSession session){

        session.setAttribute("activeUser",customerService.getActiveCustomer(principal));
        model.addAttribute("productPageResponseDTO",productService.getPageOfProducts(0,2));
        model.addAttribute("banners",bannerService.getAllBanner());

        return "public/home";
    }



//    @GetMapping("page/{pageNumber}")
//    public String  getHomePage(@PathVariable Integer pageNumber, Model model ,HttpSession session,Principal principal){
//
//        session.setAttribute("activeUser",customerService.getActiveCustomer(principal));
//        model.addAttribute("productPageResponseDTO",productService.getPageOfProducts(pageNumber,2));
//        model.addAttribute("banners",bannerService.getAllBanner());
//
//        return "public/home";
//    }


    @ResponseBody()
//    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping(value = "rest/{pageNumber}",produces ="application/json")
    public ProductPageDTO getHomePage(@PathVariable Integer pageNumber){
        System.out.println("hello----------------------");
        return productService.getPageOfProducts(pageNumber,1);
    }

    @ResponseBody()
    @GetMapping(value = "rest/",produces ="application/json")
    public ProductPageDTO getHomePage(){

        return productService.getPageOfProducts(0,1 );
    }



    //search controller-----------------------------------------
    //TODO:searching based on description,display no results,add image
    @ResponseBody
    @GetMapping("search/{query}")
    public List<ProductSearchRequest>  searchProducts(@PathVariable String query){

        List<Product> products = productService.getProductByNameContaining(query.toUpperCase());
        List<ProductSearchRequest> productSearchRequest=new ArrayList<>();
        products.forEach(product -> {

            ProductSearchRequest searchRequest = modelMapper.map(product, ProductSearchRequest.class);
            productSearchRequest.add(searchRequest );

        });

        products.forEach(product-> System.out.println(product.getProductName()));
        return  productSearchRequest;
    }







}
