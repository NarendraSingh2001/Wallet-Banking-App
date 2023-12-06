package com.fullstack.WalletBankingApp.dtoTest;

import com.fullstack.WalletBankingApp.dto.mapper.UserRegistrationMapper;
import com.fullstack.WalletBankingApp.dto.request.UserRegistrationDto;
import com.fullstack.WalletBankingApp.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRegistrationMapperTest {

    private final UserRegistrationMapper mapper = UserRegistrationMapper.MAPPER;

    @Test
    public void testUserRegistrationDtoToUser() {
        //When
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setFirstname("narendra");
        dto.setLastname("singh");
        dto.setEmail("narendra1234@gmail.com");

        User user = mapper.UserRegistrationDtoToUser(dto);

        // Then
        assertEquals(dto.getFirstname(), user.getFirstname());
        assertEquals(dto.getEmail(), user.getEmail());

    }

    @Test
    public void testUserToUserRegistrationDto() {
        // Given
        User user = new User();
        user.setFirstname("narendra");
        user.setLastname("singh");

        UserRegistrationDto dto = mapper.UserToUserRegistrationDto(user);
       //When
        assertEquals(user.getFirstname(), dto.getFirstname());
        assertEquals(user.getEmail(), dto.getEmail());

    }
}

