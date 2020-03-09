package it.step.msauth.service;

import it.step.msauth.model.AuthUserDto;
import it.step.msauth.model.TokensSaver;

public interface AuthUserService {
    AuthUserDto signUp(AuthUserDto authUserDto);

    TokensSaver signIn(AuthUserDto authUserDto);
}
