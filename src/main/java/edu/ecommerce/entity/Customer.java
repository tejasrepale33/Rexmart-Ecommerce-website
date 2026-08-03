package edu.ecommerce.entity;

	import jakarta.persistence.*;
	import lombok.Getter;
	import lombok.Setter;
	import org.hibernate.annotations.CreationTimestamp;

	import java.time.LocalDateTime;
	import java.util.List;

	@Entity
	@Getter
	@Setter
	public class Customer {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    private String name;
	    private String email;
	    private String phone;

	    @CreationTimestamp
	    private LocalDateTime createdAt;

	    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	    private List<Address> addresses;

	    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	    private List<Order> orders;

	    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
	    private Cart cart;

	    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
	    private Wishlist wishlist;

	    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	    private List<Review> reviews;
	}


