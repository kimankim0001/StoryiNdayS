package com.sparta.storyindays.dto.user.admin;

import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.enums.user.State;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminStateResDto {
    private State state;

    public AdminStateResDto(User user) {
        this.state = user.getState();
    }
}
