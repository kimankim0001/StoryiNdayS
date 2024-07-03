package com.sparta.storyindays.dto.comment;

import com.sparta.storyindays.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResDto {

    private Long commentId;

    private String username;

    private String comment;

    private Long commentLikes;

    private LocalDateTime createdAt;

    public CommentResDto(Comment comment1) {
        this.commentId = comment1.getId();
        this.username = comment1.getUser().getUsername();
        this.comment = comment1.getComment();
        this.commentLikes = comment1.getCommentLikes();
        this.createdAt = comment1.getCreatedAt();
    }
}
