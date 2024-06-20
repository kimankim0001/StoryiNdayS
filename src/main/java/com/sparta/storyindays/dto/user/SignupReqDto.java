package com.sparta.storyindays.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupReqDto {

    @NotNull(message = "username 값이 필수로 들어있어야 합니다.")
    @Size(min=4, max =10, message = "username은 최소 4자이상, 10자 이하이며 소문자와 숫자만 가능합니다.")
    @Pattern(regexp = "^[a-z0-9]*$")
    private String username;

    @NotNull(message = "password 값이 필수로 들어있어야 합니다.")
    @Size(min=8, max=15, message = "password는 최소 8자 이상, 15자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",message = "password는 알파벳 대소문자, 숫자, 특수문자가 최소 한개 씩 포함되어야 합니다.")
    private String password;

    @NotNull(message = "name 값이 필수로 들어있어야 합니다.")
    private String name;

    @Email(message = "Email 형식이어야 합니다.")
    private String email;

    @NotNull(message = "auth 값이 필수로 들어있어야 합니다.")
    private Auth authType;
}
