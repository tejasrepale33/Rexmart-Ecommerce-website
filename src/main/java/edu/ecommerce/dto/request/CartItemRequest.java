package edu.ecommerce.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
    private int customerId;
    private int productId;
    private int quantity;
}