package com.fullstack.WalletBankingApp.serviceTest;

import com.fullstack.WalletBankingApp.Enum.TransactionType;
import com.fullstack.WalletBankingApp.exception.InsufficientBalanceException;
import com.fullstack.WalletBankingApp.model.Transaction;
import com.fullstack.WalletBankingApp.model.Wallet;
import com.fullstack.WalletBankingApp.repository.WalletRepository;
import com.fullstack.WalletBankingApp.service.TransactionService;
import com.fullstack.WalletBankingApp.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private WalletRepository walletRepository;
    @Mock
   private Wallet wallet;
    @Test
    public void testRechargeWallet() {

        // Given
        Wallet wallet = new Wallet();
        wallet.setBalance(200.0);
        double rechargeAmount = 50.0;
        Transaction mockTransaction = new Transaction();
        mockTransaction.setId("45678ty6t45699");

       // When
        when(transactionService.createTransaction(wallet, rechargeAmount, TransactionType.DEPOSIT))
                .thenReturn(mockTransaction);
        when(walletRepository.save(wallet)).thenReturn(wallet);

        // Then,Actual response
        double newBalance = walletService.rechargeWallet(wallet, rechargeAmount);

        //Then, Assert
        assertEquals(250.0, newBalance);
        assertEquals(250.0, wallet.getBalance());
        verify(transactionService, times(1)).createTransaction(wallet, rechargeAmount, TransactionType.DEPOSIT);
        verify(walletRepository, times(1)).save(wallet);
    }


    @Test
    public void testTransferAmount_Success() throws InsufficientBalanceException {

        // Given
        Wallet senderWallet = new Wallet();
        senderWallet.setBalance(500.0);
        Wallet receiverWallet = new Wallet();
        receiverWallet.setBalance(300.0);
        double transferAmount = 50.0;
        Transaction mockSenderTransaction = new Transaction();
        mockSenderTransaction.setId("sender1234");
        mockSenderTransaction.setAmount(50.0);
        Transaction mockReceiverTransaction = new Transaction();
        mockReceiverTransaction.setId("receiver1234");
        mockReceiverTransaction.setAmount(50.0);

        // When
        when(transactionService.createTransaction(senderWallet, transferAmount, TransactionType.SEND))
                .thenReturn(mockSenderTransaction);
        when(transactionService.createTransaction(receiverWallet, transferAmount, TransactionType.RECEIVE))
                .thenReturn(mockReceiverTransaction);
        when(walletRepository.save(senderWallet)).thenReturn(senderWallet);
        when(walletRepository.save(receiverWallet)).thenReturn(receiverWallet);

        // Actual response
        double newSenderBalance = walletService.transferAmount(senderWallet, receiverWallet, transferAmount);

        // Then
        assertEquals(450, newSenderBalance);
        assertEquals(350, receiverWallet.getBalance());
        verify(transactionService, times(1)).createTransactionTransfer(senderWallet,receiverWallet, transferAmount, TransactionType.SEND);
        verify(transactionService, times(1)).createTransactionTransfer(receiverWallet,senderWallet, transferAmount, TransactionType.RECEIVE);
        verify(walletRepository, times(1)).save(senderWallet);
        verify(walletRepository, times(1)).save(receiverWallet);
    }

    @Test
    public void testTransferAmount_InsufficientBalance() {
        // Given
        Wallet senderWallet = new Wallet();
        senderWallet.setBalance(100.0);
        Wallet receiverWallet = new Wallet();
        receiverWallet.setBalance(50.0);
        double transferAmount = 150.0;

        // Actual and Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            walletService.transferAmount(senderWallet, receiverWallet, transferAmount);
        });

        assertEquals(100.0, senderWallet.getBalance());
        assertEquals(50.0, receiverWallet.getBalance());
    }
}



