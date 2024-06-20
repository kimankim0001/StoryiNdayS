package com.sparta.storyindays.dto.post;

import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.enums.post.PostType;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostGetResDto {

    List<PostUpdateResDto> noticePostList;
    List<PostUpdateResDto> pinnedPostList;
    Page<PostUpdateResDto> postList;

    public PostGetResDto(List<Post> noticePostList, List<Post> pinnedPostList, Page<Post> postList) {
//        this.noticePostList = noticePostList.stream().map(PostUpdateResDto::new).toList();
//        this.pinnedPostList = pinnedPostList.stream().map(PostUpdateResDto::new).toList();
        this.noticePostList = new ArrayList<>();
        this.pinnedPostList = new ArrayList<>();
        this.postList = postList.map(PostUpdateResDto::new);
    }

    public void inputTestData() {
        for(int i=0; i<2; i++) {
            PostUpdateResDto testDto = new PostUpdateResDto();
            testDto.setTitle("testNoti" + i+1);
            testDto.setContents("testContents" + i+1);
            testDto.setCreateAt(LocalDateTime.now());
            testDto.setModifiedAt(LocalDateTime.now());
            testDto.setPostType(PostType.NOTICE);
            testDto.setIsPinned(false);
            noticePostList.add(testDto);
        }

        for(int i=0; i<2; i++) {
            PostUpdateResDto testDto2 = new PostUpdateResDto();
            testDto2.setTitle("testTop" + i+1);
            testDto2.setContents("testContents" + i+1);
            testDto2.setCreateAt(LocalDateTime.now());
            testDto2.setModifiedAt(LocalDateTime.now());
            testDto2.setPostType(PostType.NORMAL);
            testDto2.setIsPinned(true);
            pinnedPostList.add(testDto2);
        }
    }
}
