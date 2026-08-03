package edu.ecommerce.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistRequest {
    private int customerId;
    private int productId;
}