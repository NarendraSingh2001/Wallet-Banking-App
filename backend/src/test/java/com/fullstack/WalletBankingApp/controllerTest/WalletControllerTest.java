package com.fullstack.WalletBankingApp.controllerTest;

import com.fullstack.WalletBankingApp.controller.WalletController;
import com.fullstack.WalletBankingApp.exception.InsufficientBalanceException;
import com.fullstack.WalletBankingApp.exception.UserNotFoundException;
import com.fullstack.WalletBankingApp.model.Transaction;
import com.fullstack.WalletBankingApp.model.Wallet;
import com.fullstack.WalletBankingApp.repository.TransactionRepository;
import com.fullstack.WalletBankingApp.repository.WalletRepository;
import com.fullstack.WalletBankingApp.service.LogoutService;
import com.fullstack.WalletBankingApp.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {

    @InjectMocks
    private WalletController walletController;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletService walletService;
    @Mock
    private LogoutService logoutService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;
    @Mock
  private TransactionRepository transactionRepository;
    @Test
    public void testRechargeWallet() {
        // Given
        String walletId = "wallet1234";
        double amount = 500.0;
        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(500.0);

        // When
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletService.rechargeWallet(wallet, amount)).thenReturn(amount);

        // actual response
        ResponseEntity<?> actual = walletController.rechargeWallet(walletId, amount);

        // Then,Assertions
        verify(walletRepository, times(1)).findById(walletId);
        verify(walletService, times(1)).rechargeWallet(wallet, amount);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEqualTo(500.0);
    }

    @Test
    public void testRechargeWalletWalletNotFound() {
        // Given
        String walletId = "wallet1234";
        double amount = 400.0;

        // When
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        // Actual Response
        ResponseEntity<?> actual = walletController.rechargeWallet(walletId, amount);

        // Then
        verify(walletRepository, times(1)).findById(walletId);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo("Wallet not found");
    }
    @Test
    public void testRechargeWalletInvalidAmount() {
        //Given
        Wallet wallet=new Wallet();
        String walletId = "wallet1234";
        double amount = -10.0;
         wallet.setWalletId(walletId);
         wallet.setBalance(amount);
        //When
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        ResponseEntity<?> response = walletController.rechargeWallet(walletId, amount);
        //Then,Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid Amount", response.getBody());

    }

    @Test
    public void testTransferAmountSuccess() throws InsufficientBalanceException {
        //Given
        String senderWalletId = "senderId";
        String senderEmail="sender@example.com";
        String receiverEmailId = "receiver@example.com";
        double amount = 700.0;
        Wallet senderWallet = new Wallet();
        senderWallet.setWalletId(senderWalletId);
        senderWallet.setEmail(senderEmail);
        senderWallet.setBalance(1000.0);
        Wallet receiverWallet = new Wallet();
        receiverWallet.setEmail(receiverEmailId);
        receiverWallet.setBalance(0.0);

        //When
        when(walletRepository.findById(senderWalletId)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByEmail(receiverEmailId)).thenReturn(Optional.of(receiverWallet));
        when(walletService.transferAmount(senderWallet, receiverWallet, amount)).thenReturn(300.0);

        //ActualResponse
        ResponseEntity<?> response = walletController.transferAmount(senderWalletId, receiverEmailId, amount);

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(senderWallet.getBalance(), response.getBody());

    }
    @Test
    public void testTransferAmountWalletNotFound() throws InsufficientBalanceException {
        //Given
        String senderWalletId = "senderId";
        String senderEmail="sender@eample.com";
        String receiverEmailId = "receiver@example.com";
        double amount = 50.0;
        Wallet senderWallet=new Wallet();
        senderWallet.setWalletId(senderWalletId);
        senderWallet.setEmail(senderEmail);

        // When
        when(walletRepository.findById(senderWalletId)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByEmail(receiverEmailId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = walletController.transferAmount(senderWalletId, receiverEmailId, amount);
        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Wallet not found", response.getBody());
    }
    @Test
    public void testTransferAmountSenderEmailAndReceiverEmailSame() throws InsufficientBalanceException {
        //Given
        String senderWalletId = "senderId";
        String senderEmail="same@example.com";
        String receiverEmailId = "same@example.com";
        double amount = 50.0;
        Wallet senderWallet=new Wallet();
        senderWallet.setWalletId(senderWalletId);
        senderWallet.setEmail(senderEmail);
        senderWallet.setBalance(500.0);
        Wallet receverWallet=new Wallet();
        receverWallet.setEmail(receiverEmailId);

        // When
        when(walletRepository.findById(senderWalletId)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByEmail(receiverEmailId)).thenReturn(Optional.of(receverWallet));

        ResponseEntity<?> response = walletController.transferAmount(senderWalletId, receiverEmailId, amount);
        //Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("senderWalletId and receiverWalletId must be different", response.getBody());
    }

    @Test
    public void testTransferAmountSenderNotHaveEnoughAmount() throws InsufficientBalanceException {
        // Given
        String senderWalletId = "sender123";
        String receiverEmailId = "naman123@gmail.com";
        double amount = 150.0;
        Wallet senderWallet = new Wallet();
        senderWallet.setWalletId(senderWalletId);
        senderWallet.setEmail("naman123@gmail.com");
        senderWallet.setBalance(100.0);

        //When
        when(walletRepository.findById(senderWalletId)).thenReturn(Optional.of(senderWallet));
        ResponseEntity<?> actualResponse = walletController.transferAmount(senderWalletId, receiverEmailId, amount);

        // Then
        verify(walletRepository, times(1)).findById(senderWalletId);
        HttpStatusCode actualStatus = actualResponse.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, actualStatus, "Expected HTTP status to be BAD_REQUEST");
        assertTrue(actualResponse.getBody() instanceof String, "Expected response body to be a String");
    }

    @Test
    public void testLogOut_Success() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Authentication authentication = mock(Authentication.class);

        // When
        String actualResponse = walletController.log_Out(request, response, authentication);

        // Then
        verify(logoutService, times(1)).logout(request, response, authentication);

        assertEquals("Logout SuccessFully", actualResponse);
    }
    @Test
    public void testShowWalletInfo_TransactionsFound() throws UserNotFoundException {
        // Given
        String walletId = "kamal12345";
        int page = 0;
        int size = 10;
        Wallet mockWallet = new Wallet();
        mockWallet.setWalletId(walletId);
        mockWallet.setEmail("kamal12345");
        Pageable paging;
        paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<Transaction> mockTransactionsPage = new PageImpl<>(Collections.singletonList(new Transaction()));

        //When
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(mockWallet));
        when(transactionRepository.findByWalletId(mockWallet.getEmail(), paging)).thenReturn(mockTransactionsPage);

        ResponseEntity<?> actualResponse = walletController.showWalletInfo(walletId, page, size);

        // Then
        verify(walletRepository, times(1)).findById(walletId);
        verify(transactionRepository, times(1)).findByWalletId(mockWallet.getEmail(), paging);

    }

    @Test
    public void testShowWalletInfo_NoTransactionsFound() throws UserNotFoundException {
        // Given
        String walletId = "kamal12345";
        int page = 0;
        int size = 10;

        Wallet mockWallet = new Wallet();
        mockWallet.setWalletId(walletId);
        mockWallet.setEmail("kamal12345");

        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<Transaction> mockTransactionsPage = new PageImpl<>(Collections.emptyList());

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(mockWallet));
        when(transactionRepository.findByWalletId(mockWallet.getEmail(), paging)).thenReturn(mockTransactionsPage);

        // When
        ResponseEntity<?> actualResponse = walletController.showWalletInfo(walletId, page, size);

        // Then
        verify(walletRepository, times(1)).findById(walletId);
        verify(transactionRepository, times(1)).findByWalletId(mockWallet.getEmail(), paging);

        HttpStatusCode actualStatus = actualResponse.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, actualStatus, "Expected HTTP status to be NOT_FOUND");
        assertTrue(actualResponse.getBody() instanceof String, "No transactions found for the wallet");
    }

    @Test
    public void testShowWalletInfoWalletNotFound() {
        // Given
        String walletId = "pavan1234";
        int page = 0;
        int size = 10;
        //When
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());
        try {
            walletController.showWalletInfo(walletId, page, size);
        } catch (UserNotFoundException e) {
            assertEquals("wallet not found", e.getMessage());
        }
    }
}