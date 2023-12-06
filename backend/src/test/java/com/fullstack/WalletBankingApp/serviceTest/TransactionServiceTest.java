package com.fullstack.WalletBankingApp.serviceTest;

import com.fullstack.WalletBankingApp.Enum.TransactionType;
import com.fullstack.WalletBankingApp.model.Transaction;
import com.fullstack.WalletBankingApp.model.Wallet;
import com.fullstack.WalletBankingApp.repository.TransactionRepository;
import com.fullstack.WalletBankingApp.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {
        @InjectMocks
        private TransactionService transactionService;

        @Mock
        private TransactionRepository transactionRepository;
        @Test
        public void testCreateTransactionRecharge() {

            // Given
            Wallet wallet = new Wallet();
            wallet.setEmail("rahul1010@mail.com");
            double amount = 150.0;
            TransactionType type = TransactionType.DEPOSIT;
            Transaction savedTransaction = new Transaction();
            savedTransaction.setEmail(wallet.getEmail());
            savedTransaction.setAmount(amount);
            savedTransaction.setType(type);
            savedTransaction.setTimestamp(savedTransaction.getTimestamp());
           // When
            when(transactionRepository.save(any())).thenReturn(savedTransaction);
            // Actual response
            Transaction createdTransaction = transactionService.createTransaction(wallet, amount, type);
            //Then, Assert
            assertEquals(wallet.getEmail(), createdTransaction.getEmail());
            assertEquals(amount, createdTransaction.getAmount());
            assertEquals(type, createdTransaction.getType());

            verify(transactionRepository, times(1)).save(any());
        }
    @Test
    public void testCreateTransactionTransfer() {
            //Given
            Wallet senderWallet=new Wallet();
            Wallet receiverWallet =new Wallet();
            senderWallet.setWalletId("wallet1234");
            receiverWallet.setEmail("receiver1234");
            double sendAmount=300.0;
        TransactionType type = TransactionType.SEND;
        Transaction savedTransaction = new Transaction();
        savedTransaction.setWalletId("wallet1234");
        savedTransaction.setAmount(sendAmount);
        savedTransaction.setType(type);
        savedTransaction.setTimestamp(savedTransaction.getTimestamp());
        //When
        when(transactionRepository.save(any())).thenReturn(savedTransaction);
        //Actual Response
        Transaction createdTransaction = transactionService.createTransactionTransfer(senderWallet,receiverWallet, sendAmount, type);
        assertEquals(senderWallet.getWalletId(), createdTransaction.getWalletId());
        assertEquals(sendAmount, createdTransaction.getAmount());
        assertEquals(type, createdTransaction.getType());
        verify(transactionRepository, times(1)).save(any());


    }
    }



