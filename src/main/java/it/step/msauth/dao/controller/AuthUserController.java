package it.step.msauth.dao.controller;

import it.step.msauth.model.AuthUserDto;
import it.step.msauth.service.AuthUserService;
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
    public AuthUserDto signIn(@Valid @RequestBody AuthUserDto authUserDto) {
        return authUserService.sighIn(authUserDto);
    }

    @PostMapping("sigh-up")
    public AuthUserDto signUp(@Valid @RequestBody AuthUserDto authUserDto) {
        return authUserService.signUp(authUserDto);
    }
}
