package com.sparta.storyindays.dto.post;

import com.sparta.storyindays.entity.Post;
import lombok.Getter;

@Getter
public class PostFollowResDto {

    private String name;

    private String title;

    private String contents;

    public PostFollowResDto(Post post){
        name = post.getUser().getName();
        title = post.getTitle();
        contents = post.getContents();
    }
}
