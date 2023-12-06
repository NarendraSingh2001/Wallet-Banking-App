package com.fullstack.WalletBankingApp.service;

import com.fullstack.WalletBankingApp.Enum.TransactionType;
import com.fullstack.WalletBankingApp.exception.InsufficientBalanceException;
import com.fullstack.WalletBankingApp.model.Transaction;
import com.fullstack.WalletBankingApp.model.Wallet;
import com.fullstack.WalletBankingApp.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionService transactionService;
   @Transactional
    public double rechargeWallet(Wallet wallet, double amount) {

        wallet.setBalance(wallet.getBalance() + amount);
        Transaction transaction = transactionService.createTransaction(wallet, amount, TransactionType.DEPOSIT);
        walletRepository.save(wallet);
        return wallet.getBalance();
    }
    @Transactional
    public double transferAmount(Wallet senderWallet, Wallet receiverWallet, double amount) throws InsufficientBalanceException {
        if (senderWallet.getBalance() >= amount && amount>0) {
            senderWallet.setBalance(senderWallet.getBalance() - amount);
            receiverWallet.setBalance(receiverWallet.getBalance() + amount);
            Transaction tempSenderTransaction = transactionService.createTransactionTransfer(senderWallet,receiverWallet, amount, TransactionType.SEND);
            Transaction tempReceiverTransaction = transactionService.createTransactionTransfer(receiverWallet,senderWallet, amount, TransactionType.RECEIVE);
            walletRepository.save(senderWallet);
            walletRepository.save(receiverWallet);
            return senderWallet.getBalance();
        } else
            throw new InsufficientBalanceException();

    }
}
