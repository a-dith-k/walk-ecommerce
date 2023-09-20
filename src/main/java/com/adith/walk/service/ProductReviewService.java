package com.adith.walk.service;

import com.adith.walk.Entities.Customer;
import com.adith.walk.Entities.Product;
import com.adith.walk.Entities.ProductReview;
import com.adith.walk.repositories.ReviewRepository;
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
        productReview.setIsApproved(false);

        reviewRepository.save(productReview);
    }

    @Override
    public List<ProductReview> getAllReviewsOfProduct(Product product) {
        return reviewRepository.getProductReviewsByProductAndIsApprovedTrue(product);
    }

    @Override
    public ProductReview getProductReviewByProductAndCustomer(Integer productId, Principal principal) {
        return reviewRepository.findProductReviewByProductAndAndCustomer(productService.getProductById(productId),getCustomer(principal)).orElseThrow();

    }

    @Override
    public boolean isProductAlreadyReviewed(Principal principal, Product product) {
       return reviewRepository.findProductReviewByProductAndAndCustomer(product, getCustomer(principal)).isPresent();
    }

    @Override
    public void updateReview(ProductReview productReview) {
        System.out.println("re-----------------------------------------------------------------------");
            reviewRepository.save(productReview);
    }

    @Override
    public List<ProductReview> getAllPendingReviews() {
        return reviewRepository.findProductReviewByIsApprovedFalse();
    }

    @Override
    public void deleteReview(Long reviewId) {
            reviewRepository.deleteById(reviewId);
    }

    @Override
    public void approveReview(Long reviewId) {

        reviewRepository.approveReviewById(reviewId);
    }

    @Override
    public Long getAggregate(Integer productId) {
        System.out.println("here in service--------------------------------------");
        return reviewRepository.findReviewAggregate(productService.getProductById(productId)).orElse(0L);
    }


    private Customer getCustomer(Principal principal){
      return   customerService.getCustomerByMobile(principal.getName());
    }
}
