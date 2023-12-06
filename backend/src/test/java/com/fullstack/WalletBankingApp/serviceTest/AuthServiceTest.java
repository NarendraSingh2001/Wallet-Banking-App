package com.fullstack.WalletBankingApp.serviceTest;

import com.fullstack.WalletBankingApp.Enum.Role;
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
import com.fullstack.WalletBankingApp.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRegistrationMapper userRegistrationMapper;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private JwtService jwtService;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
   private AuthenticationManager authenticationManager;
    @Test
    public void testRegisterUser_Success() {
        //Given
        UserRegistrationDto request = new UserRegistrationDto();
        request.setFirstname("raman");
        request.setLastname("kumar");
        request.setPassword("password@1234");
        request.setEmail("ram1234@gmail.com");
        User user =new User();
        user.setFirstname("raman");
        user.setLastname("kumar");
        user.setPassword("password@1234");
        user.setEmail("ram1234@gmail.com");
        user.setRole(Role.USER);
        String jwtToken="token";
        Token expectedToken = new Token();
        expectedToken.setToken(jwtToken);
        expectedToken.setTokenType(TokenType.BEARER);
        expectedToken.setRevoked(false);
        expectedToken.setExpired(false);

       //   When
        when(userRepository.findById(request.getEmail())).thenReturn(Optional.empty());
        when(userRegistrationMapper.UserRegistrationDtoToUser(request)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("token");
        when(authService.saveUserToken(user,jwtToken)).thenReturn(true);

        //Actual response
        AuthenticationResponse response = authService.register(request);
        boolean result = authService.saveUserToken(user, jwtToken);

        //Then, Assertion
        assertTrue(result);
        assertNotNull(response);
        assertEquals("token",response.getJwtToken());
        assertEquals("raman kumar",response.getName());
        assertEquals(0,response.getBalance());
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        // Given
        UserRegistrationDto request = new UserRegistrationDto();
        request.setFirstname("raman");
        request.setLastname("kumar");
        request.setPassword("password@1234");
        request.setEmail("ram1234@gmail.com");
        // When
        when(userRepository.findById(request.getEmail())).thenReturn(Optional.ofNullable(new User()));

        // Actual response
         AuthenticationResponse response = authService.register(request);
        System.out.println(response);
        // Assert
        assertNull(response);

    }

    @Test()
    public void testRegisterUser_RuntimeExceptionHandle() {
        // Given
        UserRegistrationDto request=new UserRegistrationDto();
        String email="pavan890@gmail.com";
        when(userRepository.findById(email)).thenThrow(new RuntimeException());

        // Actual response
        assertThrows(RuntimeException.class, () -> {
            authService.register(request);
        });
    }


    @Test
    public void testLogin_Success() throws UserNotFoundException {
        // Given
        UserLoginDto loginRequest = new UserLoginDto();
        loginRequest.setEmail("naman890@gmail.com");
        loginRequest.setPassword("password1234");
        User user = new User();
        user.setFirstname("naman");
        user.setLastname("singh");
        user.setEmail("naman890@gmail.com");
        user.setPassword("password1234");
        user.setRole(Role.USER);
        Wallet wallet = new Wallet();
        wallet.setBalance(200.00);
        wallet.setWalletId("naman1111");
        wallet.setEmail("naman890@gmail.com");
        String token = "token";
        // When
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findById("naman890@gmail.com")).thenReturn(Optional.of(user));
        when(walletRepository.findByEmail("naman890@gmail.com")).thenReturn(Optional.of(wallet));
        when(jwtService.generateToken(user)).thenReturn(token);

        // Actual response
        AuthenticationResponse response = authService.login(loginRequest);
        System.out.println(response.getClass());
        // Then,Assertion
        assertNotNull(response);
        assertEquals("naman singh", response.getName());
        assertEquals(200.00, response.getBalance());
        assertEquals("token", response.getJwtToken());
        assertEquals("naman1111",response.getWalletId());

    }

    @Test
    public void testLogin_UserNotFound() {
        // Given
        UserLoginDto loginRequest = new UserLoginDto();
        loginRequest.setEmail("saleem123@gmail.com");
        loginRequest.setPassword("password2222");
        // When
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(null);
        when(userRepository.findById("saleem123@gmail.com")).thenReturn(Optional.empty());

        // Then,Assertion
        assertThrows(UserNotFoundException.class, () -> {
            authService.login(loginRequest);
        });
    }

    @Test
    public void testLogin_UserNotFoundException() {
        //Given
        UserLoginDto loginRequest = new UserLoginDto();
        loginRequest.setEmail("jiwan234@gmail.com");
        loginRequest.setPassword("password1234");

       //When
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        //Then,Assertion
        assertThrows(UserNotFoundException.class, () -> {
            authService.login(loginRequest);
        });
    }
}


