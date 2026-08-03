package edu.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ecommerce.Repository.CartItemRepository;
import edu.ecommerce.Repository.CartRepository;
import edu.ecommerce.Repository.CustomerRepository;
import edu.ecommerce.Repository.ProductRepository;
import edu.ecommerce.dto.request.CartItemRequest;
import edu.ecommerce.dto.response.CartItemResponse;
import edu.ecommerce.dto.response.CartResponse;
import edu.ecommerce.entity.Cart;
import edu.ecommerce.entity.CartItem;
import edu.ecommerce.entity.Customer;
import edu.ecommerce.entity.Product;
import edu.ecommerce.exception.ResourceNotFoundException;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    public CartResponse getCartByCustomer(int customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for customer: " + customerId));
        return mapToCartResponse(cart);
    }

    public CartResponse addItemToCart(CartItemRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = cartRepository.findByCustomerId(request.getCustomerId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
        }

        Cart updatedCart = cartRepository.findById(cart.getId()).get();
        return mapToCartResponse(updatedCart);
    }

    public CartResponse updateCartItem(int itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        item.setQuantity(quantity);
        cartItemRepository.save(item);
        Cart cart = cartRepository.findById(item.getCart().getId()).get();
        return mapToCartResponse(cart);
    }
    
    @Transactional
    public void removeCartItem(int itemId) {

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart item not found"));

        Cart cart = item.getCart();

        if (cart.getCartItems() != null) {
            cart.getCartItems().remove(item);
        }

        cartRepository.save(cart);

        cartItemRepository.delete(item);
    }

    public void clearCart(int customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        cartItemRepository.deleteAll(items);
    }
    
    private CartResponse mapToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setCustomerId(cart.getCustomer().getId());
        response.setCustomerName(cart.getCustomer().getName());

        List<CartItemResponse> itemResponses = cart.getCartItems().stream().map(item -> {
            CartItemResponse ir = new CartItemResponse();
            ir.setId(item.getId());
            ir.setProductId(item.getProduct().getId());
            ir.setProductName(item.getProduct().getName());
            ir.setProductImageUrl(item.getProduct().getImageUrl());
            ir.setProductPrice(item.getProduct().getPrice());
            ir.setQuantity(item.getQuantity());
            ir.setSubtotal(item.getProduct().getPrice() * item.getQuantity());
            return ir;
        }).collect(Collectors.toList());

        response.setCartItems(itemResponses);
        double total = itemResponses.stream().mapToDouble(CartItemResponse::getSubtotal).sum();
        response.setTotalPrice(total);
        return response;
    }
}