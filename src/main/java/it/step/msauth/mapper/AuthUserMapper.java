package it.step.msauth.mapper;

import it.step.msauth.dao.entity.AuthUserEntity;
import it.step.msauth.model.AuthUserDto;

public class AuthUserMapper {
    public static AuthUserDto entityToDto(AuthUserEntity entity) {
        return AuthUserDto.builder()
                .id(entity.getId())
                .password("******")
                .login(entity.getLogin())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }

    public static AuthUserEntity dtoToEntityForCreate(AuthUserDto dto) {
        return AuthUserEntity.builder()
                .login(dto.getLogin())
                .password(String.valueOf(dto.getPassword().hashCode()))
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }


}
