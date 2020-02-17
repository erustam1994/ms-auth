package it.step.msauth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AuthUserDto {
    Long id;
    @NotEmpty
    @Pattern(regexp = "[a-zA-Z0-9_.]{6,30}")
    private String login;
    @NotEmpty
    @Pattern(regexp = ".{8,30}")
    private String password;
    @Email
    private String email;
    @Pattern(regexp = "[\\d]{7,50}")
    private String phoneNumber;
}
