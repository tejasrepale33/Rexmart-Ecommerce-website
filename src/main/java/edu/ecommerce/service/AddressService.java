package edu.ecommerce.service;

import edu.ecommerce.dto.request.AddressRequest;
import edu.ecommerce.dto.response.AddressResponse;
import edu.ecommerce.entity.Address;
import edu.ecommerce.entity.Customer;
import edu.ecommerce.exception.ResourceNotFoundException;
import edu.ecommerce.Repository.AddressRepository;
import edu.ecommerce.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public AddressResponse addAddress(AddressRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setCountry(request.getCountry());
        address.setCustomer(customer);

        Address saved = addressRepository.save(address);
        return mapToResponse(saved);
    }

    public List<AddressResponse> getAddressesByCustomer(int customerId) {
        return addressRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public AddressResponse updateAddress(int id, AddressRequest request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setCountry(request.getCountry());
        Address updated = addressRepository.save(address);
        return mapToResponse(updated);
    }

    public void deleteAddress(int id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        addressRepository.delete(address);
    }

    private AddressResponse mapToResponse(Address address) {
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setStreet(address.getStreet());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPincode(address.getPincode());
        response.setCountry(address.getCountry());
        response.setCustomerId(address.getCustomer().getId());
        return response;
    }
}