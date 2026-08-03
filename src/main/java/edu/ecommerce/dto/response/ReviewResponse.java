package edu.ecommerce.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponse {
    private int id;
    private int rating;
    private String comment;
    private String customerName;
    private int productId;
    private LocalDateTime createdAt;
}