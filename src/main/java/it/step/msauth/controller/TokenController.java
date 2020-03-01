package it.step.msauth.controller;

import it.step.msauth.mapper.HttpHeadersMapper;
import it.step.msauth.model.TokenType;
import it.step.msauth.model.TokensSaver;
import it.step.msauth.service.TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("token")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("verify")
    public ResponseEntity<Void> verify(@RequestHeader("Auth-Token") String token) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("User-Id", tokenService.verifyToken(token).toString());
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping("logout")
    public void logout(@RequestHeader("Auth-Token") String token) {
        tokenService.logout(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<Void> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        TokensSaver tokens = tokenService.refreshTokens(refreshToken);
        return ResponseEntity.ok().headers(HttpHeadersMapper.tokenSaverToHttpsHeaders(tokens)).build();
    }
}
