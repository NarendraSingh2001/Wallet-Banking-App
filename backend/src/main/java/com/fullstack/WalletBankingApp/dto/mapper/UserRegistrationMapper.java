package com.fullstack.WalletBankingApp.dto.mapper;

import com.fullstack.WalletBankingApp.dto.request.UserRegistrationDto;
import com.fullstack.WalletBankingApp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserRegistrationMapper {
UserRegistrationMapper MAPPER = Mappers.getMapper(UserRegistrationMapper.class);

    User UserRegistrationDtoToUser(UserRegistrationDto userRegistrationDto);

    UserRegistrationDto UserToUserRegistrationDto (User user);


}