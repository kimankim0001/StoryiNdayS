package com.sparta.storyindays.dto.comment;

import lombok.Getter;

@Getter
public class CommentUpdateReqDto {

    private String comment;

    public CommentUpdateReqDto(String comment) {
        this.comment = comment;
    }
}
