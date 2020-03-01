package it.step.msauth.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import it.step.msauth.dao.TokenRepository;
import it.step.msauth.dao.entity.TokenEntity;
import it.step.msauth.mapper.LocalDateTimeMapper;
import it.step.msauth.model.TokenType;
import it.step.msauth.model.TokensSaver;
import it.step.msauth.model.exception.TokenException;
import it.step.msauth.service.TokenService;
import it.step.msauth.util.KeyGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final KeyGeneratorUtil keyGeneratorUtil;
    private final TokenRepository tokenRepository;

    public TokenServiceImpl(KeyGeneratorUtil keyGeneratorUtil, TokenRepository tokenRepository) {
        this.keyGeneratorUtil = keyGeneratorUtil;
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public TokensSaver getTokens(Long userId) throws JOSEException, ParseException {
        tokenRepository.deactivateTokensById(userId);
        return TokensSaver.builder()
                .userId(userId)
                .token(generateAndSaveToken(userId, TokenType.AUTH_TOKEN).serialize())
                .refreshToken(generateAndSaveToken(userId, TokenType.REFRESH_TOKEN).serialize()).build();
    }

    @Override
    public Long verifyToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

            if (isTokenVerified(signedJWT, TokenType.AUTH_TOKEN)) {
                Long id = jwtClaimsSet.getLongClaim("id");
                if (id != null) return id;
            }
        } catch (ParseException | JOSEException | RuntimeException e) {
            e.printStackTrace();
        }
        throw new TokenException("auth.invalid_token");
    }

    @Override
    @Transactional
    public void logout(String token) {
        Long userId = verifyToken(token);
        tokenRepository.deactivateTokensById(userId);
    }

    @Override
    @Transactional
    public TokensSaver refreshTokens(String refreshToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(refreshToken);
            if (isTokenVerified(signedJWT, TokenType.REFRESH_TOKEN)) {
                Long userId = signedJWT.getJWTClaimsSet().getLongClaim("id");
                tokenRepository.deactivateTokensById(userId);
                return getTokens(userId);
            }
        } catch (ParseException | JOSEException | RuntimeException e) {
            e.printStackTrace();
        }
        throw new TokenException("auth.invalid_token");
    }

    private boolean isTokenVerified(SignedJWT signedJWT, TokenType type) throws JOSEException, ParseException {
        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

        JWSVerifier jwsVerifier = new RSASSAVerifier((RSAPublicKey)keyGeneratorUtil.getPublicKey());
        boolean isVerified = jwsVerifier.verify(signedJWT.getHeader(), signedJWT.getSigningInput(), signedJWT.getSignature());

        String uuid = UUID.fromString(signedJWT.getJWTClaimsSet().getStringClaim("uuid")).toString();
        boolean isNotLogout = tokenRepository.isValidToken(uuid, LocalDateTime.now(), type);

        return isNotLogout && isVerified && isNotTimeOut(jwtClaimsSet);
    }

    private JWTClaimsSet generateClaimSetForToken(Long id, TokenType type) {
        LocalDateTime localDateTime = (type == TokenType.AUTH_TOKEN) ? LocalDateTime.now().plusMinutes(10) : LocalDateTime.now().plusDays(1);
        return new JWTClaimsSet.Builder()
                .claim("id", id)
                .claim("expired", localDateTime.toString())
                .claim("uuid", UUID.randomUUID().toString())
                .build();
    }

    private SignedJWT generateAndSaveToken(Long userId, TokenType type) throws JOSEException, ParseException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);
        JWTClaimsSet authTokenClaimsSet = generateClaimSetForToken(userId, type);
        RSASSASigner rsa = new RSASSASigner(keyGeneratorUtil.getPrivateKey());
        SignedJWT token = new SignedJWT(header, authTokenClaimsSet);
        token.sign(rsa);
        saveTokenByClaimsSet(authTokenClaimsSet, type);
        return token;
    }

    private void saveTokenByClaimsSet(JWTClaimsSet claimsSet, TokenType type) throws ParseException {
        tokenRepository.save(TokenEntity.builder()
                .userId(claimsSet.getLongClaim("id"))
                .uuid(claimsSet.getStringClaim("uuid"))
                .expiredDate(LocalDateTimeMapper.stringToLocalDateTime(claimsSet.getStringClaim("expired")))
                .type(type)
                .build());
    }

    private boolean isNotTimeOut(JWTClaimsSet jwtClaimsSet) throws ParseException {
        String expiredDateTimeString = jwtClaimsSet.getStringClaim("expired");
        LocalDateTime expiredDateTime = LocalDateTimeMapper.stringToLocalDateTime(expiredDateTimeString);
        return expiredDateTime.compareTo(LocalDateTime.now()) >= 0;
    }

    @Transactional
    public void deleteInvalidTokens() {
        tokenRepository.deleteAllByActiveFalse();
        tokenRepository.deleteAllByExpiredDateBefore(LocalDateTime.now());
    }
}
