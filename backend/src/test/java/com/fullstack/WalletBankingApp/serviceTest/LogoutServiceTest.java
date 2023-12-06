package com.fullstack.WalletBankingApp.serviceTest;

import com.fullstack.WalletBankingApp.entity.Token;
import com.fullstack.WalletBankingApp.repository.TokenRepository;
import com.fullstack.WalletBankingApp.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class LogoutServiceTest {

    @InjectMocks
       private LogoutService logoutService;
        @Mock
        private HttpServletRequest request;

        @Mock
        private HttpServletResponse response;

        @Mock
        private Authentication authentication;

        @Mock
        private TokenRepository tokenRepository;



        @Test
        public void testLogout_WithValidToken() {
            // Given
            String validToken = "Bearer validToken";
            Token storedToken = new Token();
            storedToken.setToken("validToken");
            storedToken.setExpired(false);
            storedToken.setRevoked(false);
           //When
            when(request.getHeader("Authorization")).thenReturn(validToken);
            when(tokenRepository.findByToken("validToken")).thenReturn(Optional.of(storedToken));
            when(tokenRepository.save(any())).thenReturn(storedToken);

            // Actual Response
            String result = logoutService.logout(request, response, authentication);

            // Then
            assertEquals("Logout SuccessFully", result);
            assertTrue(storedToken.isExpired());
            assertTrue(storedToken.isRevoked());
            verify(tokenRepository, times(1)).save(storedToken);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        public void testLogout_WithInvalidToken() {
            // Given
            String invalidToken = "Bearer invalidToken";

            //When
            when(request.getHeader("Authorization")).thenReturn(invalidToken);
            when(tokenRepository.findByToken("invalidToken")).thenReturn(Optional.empty());

            String result =logoutService.logout(request, response, authentication);

            //Then
            assertEquals("token is null", result);
            verify(tokenRepository, never()).save(any());
        }
    }



