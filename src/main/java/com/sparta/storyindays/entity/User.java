package com.sparta.storyindays.entity;

import com.sparta.storyindays.enums.user.Auth;
import com.sparta.storyindays.dto.user.ProfileUpdateReqDto;
import com.sparta.storyindays.enums.user.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "kakao_id", nullable = true)
    private Long kakaoId;

    @Column(name = "post_likes", nullable = false)
    private Long postLikes;

    @Column(name = "comment_likes", nullable = false)
    private Long commentLikes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<PostLike> postLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<CommentLike> commentLikeList = new ArrayList<>();


    public User(String username, String password, String name, Auth auth, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.auth = auth;
        this.email = email;
        this.postLikes = 0L;
        this.commentLikes = 0L;
    }

    public User(String username, String password, String email, Auth auth, Long kakaoId) {
        this.username = email;
        this.name = username;
        this.password = password;
        this.auth = auth;
        this.email = email;
        this.kakaoId = kakaoId;
        this.postLikes = 0L;
        this.commentLikes = 0L;
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

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }
}
