package com.sparta.storyindays.dto.user.admin;

import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.enums.user.Auth;
import com.sparta.storyindays.enums.user.State;
import lombok.Getter;

@Getter
public class AdminUsersResDto {
    private String username;
    private String name;
    private Auth auth;
    private State state;
    private String email;

    public AdminUsersResDto(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.auth = user.getAuth();
        this.state = user.getState();
        this.email = user.getEmail();
    }
}
