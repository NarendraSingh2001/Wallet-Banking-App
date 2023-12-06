package com.fullstack.WalletBankingApp.service;

import com.fullstack.WalletBankingApp.Enum.TokenType;
import com.fullstack.WalletBankingApp.dto.mapper.UserRegistrationMapper;
import com.fullstack.WalletBankingApp.dto.request.UserLoginDto;
import com.fullstack.WalletBankingApp.dto.request.UserRegistrationDto;
import com.fullstack.WalletBankingApp.entity.Token;
import com.fullstack.WalletBankingApp.exception.UserNotFoundException;
import com.fullstack.WalletBankingApp.model.AuthenticationResponse;
import com.fullstack.WalletBankingApp.model.User;
import com.fullstack.WalletBankingApp.model.Wallet;
import com.fullstack.WalletBankingApp.repository.TokenRepository;
import com.fullstack.WalletBankingApp.repository.UserRepository;
import com.fullstack.WalletBankingApp.repository.WalletRepository;
import com.fullstack.WalletBankingApp.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserRegistrationMapper userRegistrationMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TokenRepository tokenRepository;

    public AuthenticationResponse register(UserRegistrationDto request) {
        try {
            String email = request.getEmail();
            Optional<User> byEmail = userRepository.findById(email);
            if (!byEmail.isPresent()) {
                User user = userRegistrationMapper.UserRegistrationDtoToUser(request);
                userRepository.save(user);
                Wallet wallet = new Wallet();
                wallet.setEmail(request.getEmail());
                walletRepository.save(wallet);
                String token = jwtService.generateToken(user);
                saveUserToken(user, token);
                AuthenticationResponse authenticationResponse = createAuthenticationResponse(user, wallet, token);
                return authenticationResponse;
            } else {
                System.out.println(("Email Id " + email + " is already present. Please use another."));
                return (null);
            }
        } catch (Exception e) {
            throw new RuntimeException("User Registration Failed");
        }

    }


    public AuthenticationResponse login(UserLoginDto loginRequest) throws UserNotFoundException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword())
            );

            String email = loginRequest.getEmail();
            User user = userRepository.findById(email).orElseThrow(() -> new UserNotFoundException("Invalid credentials"));
            Wallet wallet = walletRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UserNotFoundException("Invalid credentials"));
            if (user != null && wallet != null) {
                String token = jwtService.generateToken(user);
                saveUserToken(user, token);
                AuthenticationResponse authenticationResponse = createAuthenticationResponse(user, wallet, token);
                return authenticationResponse;
            } else
                return null;
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("Invalid credentials");
        }
    }

    public boolean saveUserToken(User user, String jwtToken) {
        Token token = new Token();
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(false);
        token.setExpired(false);
        tokenRepository.save(token);
        return true;
    }

    private AuthenticationResponse createAuthenticationResponse(User user, Wallet wallet, String token) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setWalletId(wallet.getWalletId());
        authenticationResponse.setBalance(wallet.getBalance());
        authenticationResponse.setName(user.getFirstname() + " " + user.getLastname());
        authenticationResponse.setJwtToken(token);
        return authenticationResponse;
    }

}
