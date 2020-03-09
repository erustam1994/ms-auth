package it.step.msauth.controller;

import it.step.msauth.mapper.HttpHeadersMapper;
import it.step.msauth.model.AuthUserDto;
import it.step.msauth.model.TokensSaver;
import it.step.msauth.service.AuthUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthUserController {
    private final AuthUserService authUserService;

    public AuthUserController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @PostMapping("sigh-in")
    public ResponseEntity<Void> signIn(@Valid @RequestBody AuthUserDto authUserDto) {
        TokensSaver tokens = authUserService.signIn(authUserDto);
        return ResponseEntity.ok().headers(HttpHeadersMapper.tokenSaverToHttpsHeaders(tokens)).build();
    }

    @PostMapping("sigh-up")
    public AuthUserDto signUp(@Valid @RequestBody AuthUserDto authUserDto) {
        return authUserService.signUp(authUserDto);
    }

}
