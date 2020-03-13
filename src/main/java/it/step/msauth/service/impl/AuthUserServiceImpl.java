package it.step.msauth.service.impl;

import com.nimbusds.jose.JOSEException;
import it.step.msauth.dao.AuthUserRepository;
import it.step.msauth.dao.entity.AuthUserEntity;
import it.step.msauth.mapper.AuthUserMapper;
import it.step.msauth.model.AuthUserDto;
import it.step.msauth.model.TokensSaver;
import it.step.msauth.model.exception.AuthUserException;
import it.step.msauth.service.AuthUserService;
import it.step.msauth.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

@Service
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final Logger logger = LoggerFactory.getLogger(AuthUserServiceImpl.class);
    private final TokenService tokenService;

    public AuthUserServiceImpl(AuthUserRepository authUserRepository, TokenService tokenService) {
        this.authUserRepository = authUserRepository;
        this.tokenService = tokenService;
    }

    @Override
    public AuthUserDto signUp(AuthUserDto authUserDto) {
        AuthUserEntity authUserEntity = AuthUserMapper.dtoToEntityForCreate(authUserDto);
        if (authUserRepository.getFirstByLogin(authUserDto.getLogin()).isPresent()) {
            logger.error("auth.login_already_exists {}", authUserDto.getEmail());
            throw new AuthUserException("auth.login_already_exists");
        }
        return AuthUserMapper.entityToDto(authUserRepository.save(authUserEntity));
    }

    @Override
    public TokensSaver signIn(AuthUserDto authUserDto) {
        String login = authUserDto.getLogin();
        String password = String.valueOf(authUserDto.getPassword().hashCode());
        Optional<AuthUserEntity> authUserEntity = authUserRepository.getFirstByLoginAndPassword(login, password);

        if (authUserEntity.isPresent()) {
            try {
                return tokenService.getTokens(authUserEntity.get().getId());
            } catch (JOSEException | ParseException e) {
                logger.error("can not generate tokens", e);
            }
        }
        logger.error("auth.invalid_login_or_password {}", authUserEntity);
        throw new AuthUserException("auth.invalid_login_or_password");
    }


}
