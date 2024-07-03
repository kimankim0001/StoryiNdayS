package com.sparta.storyindays.dto.post;

import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.enums.post.PostType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdateResDto {

    private String name;

    private String title;

    private String contents;

    private Long postLikes;

    private PostType postType;

    private Boolean isPinned;

    private LocalDateTime createAt;

    private LocalDateTime modifiedAt;

    public PostUpdateResDto(Post post) {
        name = post.getUser().getName();
        title = post.getTitle();
        contents = post.getContents();
        postLikes = post.getPostLikes();
        postType = post.getPostType();
        isPinned = post.getIsPinned();
        createAt = post.getCreatedAt();
        modifiedAt = post.getModifiedAt();
    }
}
