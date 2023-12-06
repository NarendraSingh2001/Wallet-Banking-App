package com.fullstack.WalletBankingApp.service;

import com.fullstack.WalletBankingApp.Enum.TransactionType;
import com.fullstack.WalletBankingApp.model.Transaction;
import com.fullstack.WalletBankingApp.model.Wallet;
import com.fullstack.WalletBankingApp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(Wallet wallet, double amount, TransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setEmail(wallet.getEmail());
        transaction.setAmount(amount);
        transaction.setWalletId(wallet.getWalletId());
        transaction.setType(type);
        transaction.setTimestamp(getFormattedTimestamp());
        transactionRepository.save(transaction);
        return transaction;

    }
    public Transaction createTransactionTransfer(Wallet senderWallet,Wallet receiveWallet, double amount, TransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setEmail(receiveWallet.getEmail());
        transaction.setAmount(amount);
        transaction.setWalletId(senderWallet.getWalletId());
        transaction.setType(type);
        transaction.setTimestamp(getFormattedTimestamp());
        transactionRepository.save(transaction);
        return transaction;

    }
    public String getFormattedTimestamp() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());
        return  formattedDate;
    }
}
