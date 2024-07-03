package com.sparta.storyindays.entity;

import com.sparta.storyindays.dto.post.PostReqDto;
import com.sparta.storyindays.enums.post.PostType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post")
public class Post extends Timstamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "contents", nullable = false, length = 255)
    private String contents;

    @Column(name = "post_likes", nullable = false)
    private Long postLikes;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "post_type", nullable = false, length = 255)
    private PostType postType;

    @Column(name = "is_pinned", nullable = false)
    private Boolean isPinned;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<PostLike> postLikeList = new ArrayList<>();

    public Post(String title, String contents, boolean b, PostType postType, User user) {
        this.title = title;
        this.contents = contents;
        this.isPinned = b;
        this.postType = postType;
        this.user = user;
        this.postLikes = 0L;
    }

    public void setPin(boolean isPinned) {
        this.isPinned = isPinned;
    }

    public void update(PostReqDto reqDto) {
        title = reqDto.getTitle();
        contents = reqDto.getContents();
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
        //comment에서 setPost해서 외래키 설정해야함
    }

    public void increasePostLikes() {
        this.postLikes++;
    }

    public void decreasePostLikes() {
        this.postLikes--;
    }
}
