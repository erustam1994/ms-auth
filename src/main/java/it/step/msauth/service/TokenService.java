package it.step.msauth.service;

import com.nimbusds.jose.JOSEException;
import it.step.msauth.model.TokensSaver;

import java.text.ParseException;

public interface TokenService {
    TokensSaver getTokens(Long id) throws JOSEException, ParseException;

    Long verifyToken(String token);

    void logout(String token);

    TokensSaver refreshTokens(String refreshToken);

    void deleteInvalidTokens();
}
