package com.sparta.storyindays.entity;

import com.sparta.storyindays.enums.user.Auth;
import com.sparta.storyindays.dto.user.ProfileUpdateReqDto;
import com.sparta.storyindays.enums.user.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class User extends Timstamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "introduction", nullable = true)
    private String introduction;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state = State.ACTIVATION;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken = "";

    @Enumerated(EnumType.STRING)
    @Column(name = "auth", nullable = false)
    private Auth auth;

    @Column(name = "email", nullable = false)
    private String email;

    public User(String username, String password, String name, Auth auth, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.auth = auth;
        this.email = email;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void update(ProfileUpdateReqDto profileUpdateReqDto) {
        this.name = profileUpdateReqDto.getName();
        this.introduction = profileUpdateReqDto.getInstroduction();
    }

    public void passwordUpdate(String password) {
        this.password = password;
    }

    public void authUpdate(Auth auth) {
        this.auth = auth;
    }

    public void stateUpdate(State state) {
        this.state = state;
    }
}
