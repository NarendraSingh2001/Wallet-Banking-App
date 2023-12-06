package com.fullstack.WalletBankingApp.configuration;


import com.fullstack.WalletBankingApp.dto.mapper.UserRegistrationMapper;
import com.fullstack.WalletBankingApp.dto.mapper.UserRegistrationMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class mapperConfiguration {
    @Bean
    public UserRegistrationMapper userRegistrationMapper() {
        return new UserRegistrationMapperImpl(); // You should adjust this based on your actual Mapper implementation
    }
}
