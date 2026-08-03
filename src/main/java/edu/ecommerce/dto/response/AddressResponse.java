package edu.ecommerce.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {
    private int id;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private int customerId; 
}