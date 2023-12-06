package com.fullstack.WalletBankingApp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserLoginDto {
    @Pattern( regexp = "^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z]{2,4}$",message = "Invalid email format")
    private String email;
    @Size(min = 8,message = "Password must have atleast 8 characters")
    private String password;

    public UserLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserLoginDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
