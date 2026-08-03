package edu.ecommerce.controller;

import edu.ecommerce.dto.request.CartItemRequest;
import edu.ecommerce.dto.response.CartResponse;
import edu.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable int customerId) {
        return ResponseEntity.ok(cartService.getCartByCustomer(customerId));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItemToCart(request));
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<CartResponse> updateCartItem(@PathVariable int itemId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateCartItem(itemId, quantity));
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<String> removeCartItem(@PathVariable int itemId) {
        cartService.removeCartItem(itemId);
        return ResponseEntity.ok("Item removed from cart");
    }

    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<String> clearCart(@PathVariable int customerId) {
        cartService.clearCart(customerId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}