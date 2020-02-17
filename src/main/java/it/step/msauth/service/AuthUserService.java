package it.step.msauth.service;

import it.step.msauth.model.AuthUserDto;

public interface AuthUserService {
    AuthUserDto signUp(AuthUserDto authUserDto);

    AuthUserDto sighIn(AuthUserDto authUserDto);
}
