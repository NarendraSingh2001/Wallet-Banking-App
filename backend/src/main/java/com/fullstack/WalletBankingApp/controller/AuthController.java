package com.fullstack.WalletBankingApp.controller;

import com.fullstack.WalletBankingApp.dto.request.UserLoginDto;
import com.fullstack.WalletBankingApp.dto.request.UserRegistrationDto;
import com.fullstack.WalletBankingApp.exception.UserNotFoundException;
import com.fullstack.WalletBankingApp.model.AuthenticationResponse;
import com.fullstack.WalletBankingApp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> Signup(@RequestBody @Validated UserRegistrationDto request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        AuthenticationResponse response = authService.register(request);

        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            String errorMessage = "User already exist with this email.Please try with other email";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody @Validated UserLoginDto request) throws UserNotFoundException {
    return    ResponseEntity.ok( authService.login(request));

    }
}






