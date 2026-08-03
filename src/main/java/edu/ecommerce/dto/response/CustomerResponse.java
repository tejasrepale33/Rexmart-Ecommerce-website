package edu.ecommerce.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CustomerResponse {
    private int id;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
}