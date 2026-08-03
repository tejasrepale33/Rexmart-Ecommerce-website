package edu.ecommerce.dto.response;

import edu.ecommerce.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private int id;
    private int customerId;
    private String customerName;
    private List<ProductResponse> products;
    private double totalPrice;
    private int quantity;
    private OrderStatus status;
    private String deliveryAddress;
    private LocalDateTime orderDate;
    private String paymentMethod;

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}