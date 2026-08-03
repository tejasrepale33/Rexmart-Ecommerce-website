package edu.ecommerce.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private double price;
    private int stock;
    private String category;
    private String imageUrl;
    private String description;
}