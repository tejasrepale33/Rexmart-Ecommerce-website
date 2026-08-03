package edu.ecommerce.service;

import edu.ecommerce.dto.response.OrderResponse;
import edu.ecommerce.dto.response.ProductResponse;
import edu.ecommerce.entity.*;
import edu.ecommerce.exception.ResourceNotFoundException;
import edu.ecommerce.Repository.AddressRepository;
import edu.ecommerce.Repository.CartItemRepository;
import edu.ecommerce.Repository.CartRepository;
import edu.ecommerce.Repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    public OrderResponse placeOrder(int cartId, int addressId, String paymentMethod) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found with id: " + cartId));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found with id: " + addressId));

        // Always get latest items directly from database
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);

        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        order.setStatus(OrderStatus.PENDING);
        order.setDeliveryAddress(address);

        String method =
                (paymentMethod != null &&
                 paymentMethod.equalsIgnoreCase("ONLINE"))
                        ? "ONLINE"
                        : "COD";

        order.setPaymentMethod(method);

        List<Product> products = cartItems.stream()
                .map(CartItem::getProduct)
                .collect(Collectors.toList());

        order.setProducts(products);

        int totalQty = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        double totalPrice = cartItems.stream()
                .mapToDouble(item ->
                        item.getProduct().getPrice() * item.getQuantity())
                .sum();

        order.setQuantity(totalQty);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        // Remove all items from cart after order
        cartItemRepository.deleteAll(cartItems);

        return mapToOrderResponse(savedOrder);
    }
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToOrderResponse(order);
    }

    public List<OrderResponse> getOrdersByCustomer(int customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse updateOrderStatus(int id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        order.setStatus(status);
        return mapToOrderResponse(orderRepository.save(order));
    }

    public void cancelOrder(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setCustomerName(order.getCustomer().getName());
        response.setTotalPrice(order.getTotalPrice());
        response.setQuantity(order.getQuantity());
        response.setStatus(order.getStatus());
        response.setOrderDate(order.getOrderDate());
        response.setPaymentMethod(order.getPaymentMethod());

        if (order.getDeliveryAddress() != null) {
            Address a = order.getDeliveryAddress();
            response.setDeliveryAddress(
                a.getStreet() + ", " + a.getCity() + ", " +
                a.getState() + " - " + a.getPincode() + ", " + a.getCountry()
            );
        }

        List<ProductResponse> products = order.getProducts().stream()
                .map(p -> modelMapper.map(p, ProductResponse.class))
                .collect(Collectors.toList());
        response.setProducts(products);
        return response;
    }
}