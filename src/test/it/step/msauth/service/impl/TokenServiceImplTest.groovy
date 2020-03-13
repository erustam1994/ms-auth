package it.step.msauth.service.impl

import com.nimbusds.jose.JOSEException
import com.nimbusds.jwt.SignedJWT
import it.step.msauth.dao.TokenRepository
import it.step.msauth.model.TokenType
import it.step.msauth.model.exception.TokenException
import it.step.msauth.util.KeyGeneratorUtil
import spock.lang.Specification

import java.text.ParseException

class TokenServiceImplTest extends Specification {

    private KeyGeneratorUtil keyGeneratorUtil = new KeyGeneratorUtil();
    private TokenRepository tokenRepository = Mock();
    private TokenServiceImpl tokenService = new TokenServiceImpl(keyGeneratorUtil, tokenRepository)

    def "GetTokens"() {
        //???
    }

    def "VerifyTokenIsValidToken"() {
        given:
        def token = "eyJhbGciOiJSUzI1NiJ9.eyJleHBpcmVkIjoiMjAyMC0wMy0wMlQxNzozMjo1Ny41MDM5NjMzMDAiLCJpZCI6MSwidXVpZCI6ImY2NzE3NWEzLTQ4NzUtNDhlOC04NDFmLWI5YzEzNDYzNjVjNyJ9.AOZPzdSUT6vg2HgZyvsnUD459dE-eFoeiq4VZwYipLIld688ZPdTV_563MCFkd_G6KCct0x6FJ-pXWca7mBrFKJdPHk8NxDNMUnpuGpW61L5RP-Gfa8TV4qTXNXIz6PINWBWqTPdIXScM2Uwp_F6YqkAbMevksUvGwFiWJWh6ufOL6Su8YQXjkTZOYhqML-ODO60VypwJdLpc7VD12Ru0xw9hI9ueYSqYekzsHM6d5k_bqh2fYl5tAJH8HM5JnheuQQRchfZh0IrN6GroMXJUyISC_6VKKfFArH4MICrFjES3FzmvRTzMJSjHuG7LCCLKkj3zUv71oKSEI7XfZOgYg"
        when:
        TokenServiceImpl tokenService = new TokenServiceImpl(keyGeneratorUtil, tokenRepository) {
            @Override
            boolean isTokenVerified(SignedJWT signedJWT, TokenType type) throws JOSEException, ParseException {
                return true;
            }
        }
        def result = tokenService.verifyToken(token)
        then:
        result == 1
    }

    def "VerifyTokenIsNotValidToken"() {
        given:
        def token = "eyJhbGciOiJSUzI1NiJ9.eyJleHBpcmVkIjoiMjAyMC0wMy0wMlQxNzozMjo1Ny41MDM5NjMzMDAiLCJpZCI6MSwidXVpZCI6ImY2NzE3NWEzLTQ4NzUtNDhlOC04NDFmLWI5YzEzNDYzNjVjNyJ9.AOZPzdSUT6vg2HgZyvsnUD459dE-eFoeiq4VZwYipLIld688ZPdTV_563MCFkd_G6KCct0x6FJ-pXWca7mBrFKJdPHk8NxDNMUnpuGpW61L5RP-Gfa8TV4qTXNXIz6PINWBWqTPdIXScM2Uwp_F6YqkAbMevksUvGwFiWJWh6ufOL6Su8YQXjkTZOYhqML-ODO60VypwJdLpc7VD12Ru0xw9hI9ueYSqYekzsHM6d5k_bqh2fYl5tAJH8HM5JnheuQQRchfZh0IrN6GroMXJUyISC_6VKKfFArH4MICrFjES3FzmvRTzMJSjHuG7LCCLKkj3zUv71oKSEI7XfZOgYg"
        when:
        TokenServiceImpl tokenService2 = new TokenServiceImpl(keyGeneratorUtil, tokenRepository) {
            @Override
            boolean isTokenVerified(SignedJWT signedJWT, TokenType type) throws JOSEException, ParseException {
                return false;
            }
        }
        tokenService2.verifyToken(token)
        then:
        thrown(TokenException)
    }

    def "VerifyTokenParseException"() {
        given:
        def token = "Y2roM.XJUyISC_6VKKfFAr.H4MICrFjES3FzmvRTzMJSjHuG7LCCLKkj3zUv71oKSEI7XfZOgYg"
        when:
        tokenService.verifyToken(token)
        then:
        thrown(TokenException)
    }

    def "Logout"() {
        //???
    }

    def "RefreshTokens"() {
        //???
    }

    def "DeleteInvalidTokens"() {
        //???
    }
}
