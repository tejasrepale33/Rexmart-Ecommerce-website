package edu.ecommerce.service;

import edu.ecommerce.dto.request.WishlistRequest;
import edu.ecommerce.dto.response.ProductResponse;
import edu.ecommerce.dto.response.WishlistResponse;
import edu.ecommerce.entity.Customer;
import edu.ecommerce.entity.Product;
import edu.ecommerce.entity.Wishlist;
import edu.ecommerce.exception.ResourceNotFoundException;
import edu.ecommerce.Repository.CustomerRepository;
import edu.ecommerce.Repository.ProductRepository;
import edu.ecommerce.Repository.WishlistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    public WishlistResponse getWishlist(int customerId) {
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for customer: " + customerId));
        return mapToResponse(wishlist);
    }

    public WishlistResponse addToWishlist(WishlistRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Wishlist wishlist = wishlistRepository.findByCustomerId(request.getCustomerId())
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setCustomer(customer);
                    newWishlist.setProducts(new ArrayList<>());
                    return wishlistRepository.save(newWishlist);
                });

        if (!wishlist.getProducts().contains(product)) {
            wishlist.getProducts().add(product);
            wishlistRepository.save(wishlist);
        }

        return mapToResponse(wishlist);
    }

    public WishlistResponse removeFromWishlist(WishlistRequest request) {
        Wishlist wishlist = wishlistRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));
        wishlist.getProducts().removeIf(p -> p.getId() == request.getProductId());
        wishlistRepository.save(wishlist);
        return mapToResponse(wishlist);
    }

    private WishlistResponse mapToResponse(Wishlist wishlist) {
        WishlistResponse response = new WishlistResponse();
        response.setId(wishlist.getId());
        response.setCustomerId(wishlist.getCustomer().getId());
        response.setCustomerName(wishlist.getCustomer().getName());
        List<ProductResponse> products = wishlist.getProducts().stream()
                .map(p -> modelMapper.map(p, ProductResponse.class))
                .collect(Collectors.toList());
        response.setProducts(products);
        return response;
    }
}