package edu.ecommerce.service;

import edu.ecommerce.dto.request.CustomerRequest;
import edu.ecommerce.dto.response.CustomerResponse;
import edu.ecommerce.entity.Customer;
import edu.ecommerce.exception.ResourceNotFoundException;
import edu.ecommerce.Repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CustomerResponse addCustomer(CustomerRequest request) {
        Customer customer = modelMapper.map(request, Customer.class);
        Customer saved = customerRepository.save(customer);
        return modelMapper.map(saved, CustomerResponse.class);
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CustomerResponse.class))
                .collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return modelMapper.map(customer, CustomerResponse.class);
    }

    public CustomerResponse updateCustomer(int id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        modelMapper.map(request, customer);
        Customer updated = customerRepository.save(customer);
        return modelMapper.map(updated, CustomerResponse.class);
    }

    public void deleteCustomer(int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }
    
    public CustomerResponse getByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) throw new ResourceNotFoundException("Customer not found");
        return modelMapper.map(customer, CustomerResponse.class);
    }
}