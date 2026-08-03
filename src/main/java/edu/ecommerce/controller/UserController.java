package edu.ecommerce.controller;

import edu.ecommerce.config.JwtUtil;
import edu.ecommerce.dto.request.LoginRequest;
import edu.ecommerce.dto.response.AuthResponse;
import edu.ecommerce.entity.User;
import edu.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(user.getEmail());
        
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setUserId(user.getId());
        if (user.getCustomer() != null) {
            response.setCustomerId(user.getCustomer().getId());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody LoginRequest request) {
        User user = userService.register(request.getEmail(), request.getPassword(), "CUSTOMER");
        String token = jwtUtil.generateToken(user.getEmail());
        
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setUserId(user.getId());
        if (user.getCustomer() != null) {
            response.setCustomerId(user.getCustomer().getId());
        }
        return ResponseEntity.ok(response);
    }
}