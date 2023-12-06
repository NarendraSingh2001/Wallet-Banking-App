package com.fullstack.WalletBankingApp.exception;

public class InsufficientBalanceException extends Exception {

    public InsufficientBalanceException() {
        super("Insufficient balance in the wallet.");
    }

}
