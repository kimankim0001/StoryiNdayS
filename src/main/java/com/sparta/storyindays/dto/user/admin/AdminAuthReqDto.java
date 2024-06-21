package com.sparta.storyindays.dto.user.admin;

import com.sparta.storyindays.enums.user.Auth;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminAuthReqDto {
    private Auth auth;
}
