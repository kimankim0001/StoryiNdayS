package com.sparta.storyindays.dto.user;

import com.sparta.storyindays.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateResDto {
    private String name;
    private String instroduction;

    public ProfileUpdateResDto(User user) {
        this.name = user.getName();
        this.instroduction = user.getIntroduction();
    }
}
