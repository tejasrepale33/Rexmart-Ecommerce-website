package edu.ecommerce.Repository;

import edu.ecommerce.entity.Order;
import edu.ecommerce.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCustomerId(int customerId);
    List<Order> findByStatus(OrderStatus status);
}