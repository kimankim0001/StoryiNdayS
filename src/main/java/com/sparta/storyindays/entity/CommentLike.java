package com.sparta.storyindays.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comment_like")
@NoArgsConstructor
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "comment_like")
    private boolean commentLike;

    public CommentLike(Comment comment, Post post, User user, boolean commentLike) {
        this.comment = comment;
        this.post = post;
        this.user = user;
        this.commentLike = commentLike;
    }

    public void updateCommentLike(boolean commentLike) {
        this.commentLike = commentLike;
    }
}
