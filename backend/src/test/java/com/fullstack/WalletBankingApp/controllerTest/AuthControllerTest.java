package com.fullstack.WalletBankingApp.controllerTest;

import com.fullstack.WalletBankingApp.controller.AuthController;
import com.fullstack.WalletBankingApp.dto.request.UserLoginDto;
import com.fullstack.WalletBankingApp.dto.request.UserRegistrationDto;
import com.fullstack.WalletBankingApp.exception.UserNotFoundException;
import com.fullstack.WalletBankingApp.model.AuthenticationResponse;
import com.fullstack.WalletBankingApp.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@ExtendWith(SpringExtension.class)
public class AuthControllerTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSignupSuccess() {
        // Given
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setFirstname("raman");
        registrationDto.setLastname("kumar");
        registrationDto.setPassword("password@1234");
        registrationDto.setEmail("ram1234@gmail.com");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("token","raman kumar","89uh123t7872h78",0.00);

        // When
        when(passwordEncoder.encode("password@1234")).thenReturn("encodedPassword");
        when(authService.register(registrationDto)).thenReturn(expectedResponse);

        // Actual Response
        ResponseEntity<?> actualResponse = authController.Signup(registrationDto);

        // Then,Assertions
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(expectedResponse);

    }
    @Test
    public  void testSignupFailedUserAlreadyExist(){
        //Given
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setFirstname("raman");
        registrationDto.setLastname("kumar");
        registrationDto.setPassword("password@1234");
        registrationDto.setEmail("userexist@gmail.com");

        //When
        when(passwordEncoder.encode("password@1234")).thenReturn("encodedPassword");
        when(authService.register(registrationDto)).thenReturn(null);

        // Actual Response
        ResponseEntity<?> actualResponse = authController.Signup(registrationDto);

        // Then,Assertions
        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
        assertEquals("User already exist with this email.Please try with other email", actualResponse.getBody());

    }

    @Test
    public void testLoginSuccess() throws UserNotFoundException {
        // Given
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setPassword("password@1234");
        loginDto.setEmail("ram1234@gmail.com");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("token","raman kumar","89uh123t7872h78",200.00);

        //When
        when(authService.login(loginDto)).thenReturn(expectedResponse);

        //ActualResponse
        ResponseEntity<?> expected = authController.login(loginDto);

        // Then
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody()).isEqualTo(expectedResponse);
    }
}
