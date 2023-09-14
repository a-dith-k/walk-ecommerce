package com.adith.walk.service;


import com.adith.walk.Entities.Product;
import com.adith.walk.Entities.ProductReview;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface ReviewService {

    void addReview(ProductReview productReview, Integer productId, Principal principal) throws AlreadyUsedException;

List<ProductReview> getAllReviews(Product product);
}
