package edu.ecommerce.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductResponse {
    private int id;
    private String name;
    private double price;
    private int stock;
    private String category;
    private String imageUrl;
    private String description;
    private LocalDateTime createdAt;
}