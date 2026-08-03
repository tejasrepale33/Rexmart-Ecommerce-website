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
	public class Cart {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @OneToOne
	    @JoinColumn(name = "customer_id")
	    private Customer customer;
	    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	    private List<CartItem> cartItems;

	    @CreationTimestamp
	    private LocalDateTime createdAt;
	}


