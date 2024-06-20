package com.sparta.storyindays.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
@Table(name = "Comment")
public class Comment extends Timstamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "댓글 내용을 입력해 주세요.")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "Post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "User_id", nullable = false)
    private User user;

    public Comment(String comment, Post post, User user) {
        this.comment = comment;
        this.post = post;
        this.user = user;
    }


}
