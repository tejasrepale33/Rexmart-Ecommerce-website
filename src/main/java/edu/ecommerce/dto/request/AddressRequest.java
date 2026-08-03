package edu.ecommerce.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private int customerId;
}