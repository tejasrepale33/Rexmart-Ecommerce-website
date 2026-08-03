package edu.ecommerce.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartResponse {
    private int id;
    private int customerId;
    private String customerName;
    private List<CartItemResponse> cartItems;
    private double totalPrice;
}