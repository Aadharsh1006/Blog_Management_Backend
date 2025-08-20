package com.examly.springapp.controller;

import com.examly.springapp.dto.LoginDto;
import com.examly.springapp.dto.LoginResponseDto;
import com.examly.springapp.dto.RegisterDto;
import com.examly.springapp.model.Role;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto) {
    System.out.println("RegisterDto: " + registerDto.getName() + ", " + registerDto.getEmail() + ", Role: " + registerDto.getRole());
    try {
        Role userRole = registerDto.getRole() != null ? registerDto.getRole() : Role.READER;
        authService.registerUser(
            registerDto.getName(),
            registerDto.getEmail(),
            registerDto.getPassword(),
            userRole
        );
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        try {
            String token = authService.loginUser(loginDto.getEmail(), loginDto.getPassword());
            User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after login"));
            return ResponseEntity.ok(new LoginResponseDto(token, user));
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }
}
