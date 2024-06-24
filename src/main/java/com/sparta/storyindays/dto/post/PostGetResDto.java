package com.sparta.storyindays.dto.post;

import com.sparta.storyindays.entity.Post;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PostGetResDto {

    List<PostNotifyResDto> noticePostList;
    List<PostUpdateResDto> pinnedPostList;
    Page<PostUpdateResDto> postList;

    public PostGetResDto(List<Post> noticePostList, List<Post> pinnedPostList, Page<Post> postList) {
        this.noticePostList = noticePostList.stream().map(PostNotifyResDto::new).toList();
        this.pinnedPostList = pinnedPostList.stream().map(PostUpdateResDto::new).toList();
        this.postList = postList.map(PostUpdateResDto::new);
    }
}
