package com.sparta.storyindays.dto.user.admin;

import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.enums.user.State;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminStateResDto {
    private String name;
    private State state;

    public AdminStateResDto(User user) {
        this.name = user.getName();
        this.state = user.getState();
    }
}
