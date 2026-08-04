package edu.ecommerce.service;

import edu.ecommerce.entity.User;
import edu.ecommerce.entity.Customer;
import edu.ecommerce.Repository.UserRepository;
import edu.ecommerce.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String dbPassword = user.getPassword();
        boolean matches = false;
        
        if (dbPassword != null && (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$") || dbPassword.startsWith("$2y$"))) {
            matches = passwordEncoder.matches(password, dbPassword);
        } else {
            matches = password != null && password.equals(dbPassword);
        }
        
        if (!matches) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    public User register(String email, String password, String role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null) {
            user.setCustomer(customer);
        }
        
        return userRepository.save(user);
    }
}