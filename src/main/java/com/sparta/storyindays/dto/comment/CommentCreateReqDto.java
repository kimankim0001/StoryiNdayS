package com.sparta.storyindays.dto.comment;

import lombok.Getter;

@Getter
public class CommentCreateReqDto {

    private String comment;

    public CommentCreateReqDto(String comment) {
        this.comment = comment;
    }
}
