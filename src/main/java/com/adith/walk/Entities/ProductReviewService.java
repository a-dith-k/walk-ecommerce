package com.adith.walk.Entities;

import com.adith.walk.repositories.ReviewRepository;
import com.adith.walk.service.CustomerService;
import com.adith.walk.service.ProductService;
import com.adith.walk.service.ReviewService;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;


@Service
public class ProductReviewService implements ReviewService {

    final ReviewRepository reviewRepository;

    final ProductService productService;

    final CustomerService customerService;

    public ProductReviewService(ReviewRepository reviewRepository, ProductService productService, CustomerService customerService) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
        this.customerService = customerService;
    }



    @Override
    public void addReview(ProductReview productReview, Integer productId, Principal principal) throws AlreadyUsedException {

        List<ProductReview> productReviewsByProduct = reviewRepository.getProductReviewsByProduct(productService.getProductById(productId));

        boolean isAlreadyReviewed = productReviewsByProduct
                .stream()
                .anyMatch(pReview -> pReview
                        .getCustomer()
                        .equals(customerService
                                .getCustomerByMobile(principal
                                        .getName())));

        if(isAlreadyReviewed){
            throw new AlreadyUsedException("You have Already Reviewed");
        }

        productReview.setProduct(productService.getProductById(productId));
        productReview.setCustomer(customerService.getActiveCustomer(principal));

        reviewRepository.save(productReview);
    }

    @Override
    public List<ProductReview> getAllReviews(Product product) {
        return reviewRepository.getProductReviewsByProduct(product);
    }
}
