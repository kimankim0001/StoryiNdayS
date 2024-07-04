package com.sparta.storyindays.dto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentLikeResDto {

    private Long commentId;

    private String username;

    private String comment;

    private Long commentLikes;

    private LocalDateTime createdAt;

    @Builder
    public CommentLikeResDto(Long commentId, String username, String comment, Long commentLikes, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.username = username;
        this.comment = comment;
        this.commentLikes = commentLikes;
        this.createdAt = createdAt;
    }
}
