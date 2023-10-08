package com.adith.walk.service.review.service;


import com.adith.walk.entities.Product;
import com.adith.walk.entities.ProductReview;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface ReviewService {

    void addReview(ProductReview productReview, Integer productId, Principal principal) throws AlreadyUsedException;

    List<ProductReview> getAllReviewsOfProduct(Product product);

    ProductReview getProductReviewByProductAndCustomer(Integer productId, Principal principal);

    boolean isProductAlreadyReviewed(Principal principal, Product product);


    void updateReview(ProductReview productReview);

    List<ProductReview> getAllPendingReviews();

    void deleteReview(Long reviewId);

    void approveReview(Long reviewId);


    Long getAggregate(Integer productId);

    Long getCountOfPendingReviews();
}
