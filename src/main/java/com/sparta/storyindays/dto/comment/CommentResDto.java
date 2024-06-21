package com.sparta.storyindays.dto.comment;

import lombok.Getter;

@Getter
public class CommentResDto {

    private Long commentId;

    private String username;

    private String comment;

    public CommentResDto(Long commentId, String username, String comment) {
        this.commentId = commentId;
        this.username = username;
        this.comment = comment;
    }
}
