package com.fullstack.WalletBankingApp.model;

import lombok.*;

@Data
public class AuthenticationResponse {
    private  String jwtToken;
    private String name;
    private String walletId;
    private  double balance;

    public AuthenticationResponse(String jwtToken, String name, String walletId, double balance) {
        this.jwtToken = jwtToken;
        this.name = name;
        this.walletId = walletId;
        this.balance = balance;
    }

    public AuthenticationResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}


