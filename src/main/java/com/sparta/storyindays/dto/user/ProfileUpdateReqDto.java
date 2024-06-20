package com.sparta.storyindays.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileUpdateReqDto {
    private String name;

    private String instroduction;

    private String currentPassword;

    private String newPassword;
}
