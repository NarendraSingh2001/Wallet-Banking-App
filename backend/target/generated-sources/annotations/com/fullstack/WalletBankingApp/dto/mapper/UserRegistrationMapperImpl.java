package com.fullstack.WalletBankingApp.dto.mapper;

import com.fullstack.WalletBankingApp.dto.request.UserRegistrationDto;
import com.fullstack.WalletBankingApp.model.User;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-05T23:09:00+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
public class UserRegistrationMapperImpl implements UserRegistrationMapper {

    @Override
    public User UserRegistrationDtoToUser(UserRegistrationDto userRegistrationDto) {
        if ( userRegistrationDto == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( userRegistrationDto.getEmail() );
        user.setFirstname( userRegistrationDto.getFirstname() );
        user.setLastname( userRegistrationDto.getLastname() );
        user.setPassword( userRegistrationDto.getPassword() );

        return user;
    }

    @Override
    public UserRegistrationDto UserToUserRegistrationDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();

        userRegistrationDto.setFirstname( user.getFirstname() );
        userRegistrationDto.setLastname( user.getLastname() );
        userRegistrationDto.setEmail( user.getEmail() );
        userRegistrationDto.setPassword( user.getPassword() );

        return userRegistrationDto;
    }
}
