package edu.ecommerce.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponse {
    private int id;
    private int productId;
    private String productName;
    private String productImageUrl;
    private double productPrice;
    private int quantity;
    private double subtotal;
}