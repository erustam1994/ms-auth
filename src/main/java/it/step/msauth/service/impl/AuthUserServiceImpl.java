package it.step.msauth.service.impl;

import it.step.msauth.dao.AuthUserRepository;
import it.step.msauth.dao.entity.AuthUserEntity;
import it.step.msauth.mapper.AuthUserMapper;
import it.step.msauth.model.AuthUserDto;
import it.step.msauth.model.exception.AuthUserException;
import it.step.msauth.service.AuthUserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository authUserRepository;

    public AuthUserServiceImpl(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Override
    public AuthUserDto signUp(AuthUserDto authUserDto) {
        AuthUserEntity authUserEntity = AuthUserMapper.dtoToEntityForCreate(authUserDto);
        if (getUserByLogin(authUserDto.getLogin()).isPresent())
            throw new AuthUserException("auth_user.login_already_exists");
        return AuthUserMapper.entityToDto(authUserRepository.save(authUserEntity));
    }

    @Override
    public AuthUserDto sighIn(AuthUserDto authUserDto) {
        String login = authUserDto.getLogin();
        String password = String.valueOf(authUserDto.getPassword().hashCode());
        Optional<AuthUserEntity> authUserEntity = getUserByLogin(login);

        if (authUserEntity.isPresent() && authUserEntity.get().getPassword().equals(password))
            return AuthUserMapper.entityToDto(authUserEntity.get());
        else throw new AuthUserException("auth_user.invalid_login_or_password");
    }

    private Optional<AuthUserEntity> getUserByLogin(String login) {
        return authUserRepository.getFirstByLogin(login);
    }
}
