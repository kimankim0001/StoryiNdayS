package com.sparta.storyindays.dto.post;

import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.enums.post.PostType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PostReqDto {

    String title;
    String contents;

    public Post toPostEntity(User user) {
        return Post.builder()
                .title(title)
                .contents(contents)
                .isPinned(false)
                .postType(PostType.NORMAL)
                .user(user)
                .build();
    }

    public Post toNoticePostEntity(User user) {
        return Post.builder()
                .title(title)
                .contents(contents)
                .isPinned(false)
                .postType(PostType.NOTICE)
                .user(user)
                .build();
    }
}
