package com.sparta.storyindays.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "post_like")
@NoArgsConstructor
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "post_like")
    private boolean postLike;

    public PostLike(Post post, User user, boolean postLike) {
        this.post = post;
        this.user = user;
        this.postLike = postLike;
    }

    public void updatePostLike(boolean postLike) {
        this.postLike = postLike;
    }
}
