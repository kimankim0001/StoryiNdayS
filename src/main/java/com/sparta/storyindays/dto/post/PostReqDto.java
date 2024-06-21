package com.sparta.storyindays.dto.post;

import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.enums.post.PostType;
import lombok.Getter;

@Getter
public class PostReqDto {

    String title;

    String contents;

    public Post toPostEntity(User user) {
        return new Post(title, contents, false, PostType.NORMAL, user);
    }

    public Post toNoticePostEntity(User user) {
        return new Post(title, contents, false, PostType.NOTICE, user);
    }
}
