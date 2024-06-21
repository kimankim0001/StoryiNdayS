package com.sparta.storyindays.dto.post;

import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.enums.post.PostType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostNotifyResDto {

    private String name;

    private String title;

    private String contents;

    private PostType postType;

    private LocalDateTime createAt;

    private LocalDateTime modifiedAt;

    public PostNotifyResDto(Post post) {
        this.name = post.getUser().getName();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.postType = post.getPostType();
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
