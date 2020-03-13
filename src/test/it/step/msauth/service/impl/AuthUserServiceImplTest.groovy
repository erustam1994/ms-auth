package it.step.msauth.service.impl

import it.step.msauth.dao.AuthUserRepository
import it.step.msauth.dao.entity.AuthUserEntity
import it.step.msauth.mapper.AuthUserMapper
import it.step.msauth.model.AuthUserDto
import it.step.msauth.model.TokensSaver
import it.step.msauth.model.exception.AuthUserException
import it.step.msauth.service.TokenService
import spock.lang.Specification

class AuthUserServiceImplTest extends Specification {

    private AuthUserRepository authUserRepository = Mock()
    private TokenService tokenService = Mock()
    private AuthUserServiceImpl authUserService = new AuthUserServiceImpl(authUserRepository, tokenService);
    private AuthUserDto authUserDto = AuthUserDto.builder().login("john").password("abcdf111!").build()
    private AuthUserEntity authUserEntity = AuthUserEntity.builder().id(1).login("john").password("abcdf111!").build()

    def "SignUp"() {
        when:
        def result = authUserService.signUp(authUserDto)
        then:
        authUserRepository.save(AuthUserMapper.dtoToEntityForCreate(authUserDto)) >> authUserEntity
        authUserRepository.getFirstByLogin("john") >> Optional.empty()
        result == AuthUserMapper.entityToDto(authUserEntity)

        when:
        authUserService.signUp(authUserDto)
        then:
        authUserRepository.getFirstByLogin("john") >> Optional.of(authUserEntity)
        thrown(AuthUserException)
    }

    def "SighIn"() {

        given:
        def login = authUserDto.login
        def password = String.valueOf(authUserDto.password.hashCode())

        when:
        authUserService.signIn(authUserDto)
        then:
        authUserRepository.getFirstByLoginAndPassword(login, password) >> Optional.empty()
        thrown(AuthUserException)

        when:
        def result = authUserService.signIn(authUserDto)
        then:
        authUserRepository.getFirstByLoginAndPassword(login, password) >> Optional.of(authUserEntity)
        tokenService.getTokens(authUserEntity.id) >> TokensSaver.builder().userId(authUserEntity.id).build();
        result.getUserId() == authUserEntity.id
    }
}
