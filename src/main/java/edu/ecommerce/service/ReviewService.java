package edu.ecommerce.service;

import edu.ecommerce.dto.request.ReviewRequest;
import edu.ecommerce.dto.response.ReviewResponse;
import edu.ecommerce.entity.Customer;
import edu.ecommerce.entity.Product;
import edu.ecommerce.entity.Review;
import edu.ecommerce.exception.ResourceNotFoundException;
import edu.ecommerce.Repository.CustomerRepository;
import edu.ecommerce.Repository.ProductRepository;
import edu.ecommerce.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    public ReviewResponse addReview(ReviewRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCustomer(customer);
        review.setProduct(product);

        Review saved = reviewRepository.save(review);
        return mapToResponse(saved);
    }

    public List<ReviewResponse> getReviewsByProduct(int productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getReviewsByCustomer(int customerId) {
        return reviewRepository.findByCustomerId(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Double getAverageRating(int productId) {
        return reviewRepository.getAverageRating(productId);
    }

    public void deleteReview(int id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        reviewRepository.delete(review);
    }

    private ReviewResponse mapToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCustomerName(review.getCustomer().getName());
        response.setProductId(review.getProduct().getId());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }
}