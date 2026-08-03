package edu.ecommerce.controller;

import edu.ecommerce.dto.request.WishlistRequest;
import edu.ecommerce.dto.response.WishlistResponse;
import edu.ecommerce.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<WishlistResponse> getWishlist(@PathVariable int customerId) {
        return ResponseEntity.ok(wishlistService.getWishlist(customerId));
    }

    @PostMapping("/add")
    public ResponseEntity<WishlistResponse> addToWishlist(@RequestBody WishlistRequest request) {
        return ResponseEntity.ok(wishlistService.addToWishlist(request));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<WishlistResponse> removeFromWishlist(@RequestBody WishlistRequest request) {
        return ResponseEntity.ok(wishlistService.removeFromWishlist(request));
    }
}