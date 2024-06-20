package com.sparta.storyindays.dto.comment;

import com.sparta.storyindays.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResDto {

    private Long commentId;

    private String username;

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public CommentResDto(Long commentId, String username, String comment, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.commentId = commentId;
        this.username = username;
        this.comment = comment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static CommentResDto toDto(Comment comment) {
        return new CommentResDto(comment.getId(), comment.getUser().getUsername(), comment.getComment(), comment.getPost().getId(), comment.getCreatedAt(), comment.getModifiedAt());
    }

}
