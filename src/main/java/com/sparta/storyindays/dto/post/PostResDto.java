package com.sparta.storyindays.dto.post;

import com.sparta.storyindays.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResDto {

    private String name;
    private String title;
    private String contents;
    private LocalDateTime createAt;
    private boolean isPinned;

    public PostResDto(Post post) {
        name = post.getUser().getName();
        title = post.getTitle();
        contents = post.getContents();
        createAt = post.getCreatedAt();
        isPinned = post.getIsPinned();
    }
}
