package edu.ecommerce.entity;
	import jakarta.persistence.*;
	import lombok.Getter;
	import lombok.Setter;

	@Entity
	@Getter
	@Setter
	public class Address {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    private String street;
	    private String city;
	    private String state;
	    private String pincode;
	    private String country;

	    @ManyToOne
	    @JoinColumn(name = "customer_id")
	    private Customer customer;
	}

