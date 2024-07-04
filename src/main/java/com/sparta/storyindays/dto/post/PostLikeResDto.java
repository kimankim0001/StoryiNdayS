package com.sparta.storyindays.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostLikeResDto {

    private String name;

    private String title;

    private String contents;

    private Long postLikes;

    private LocalDateTime modifiedAt;

    @Builder
    public PostLikeResDto(String name, String title, String contents, Long postLikes, LocalDateTime modifiedAt) {
        this.name = name;
        this.title = title;
        this.contents = contents;
        this.postLikes = postLikes;
        this.modifiedAt = modifiedAt;
    }
}
