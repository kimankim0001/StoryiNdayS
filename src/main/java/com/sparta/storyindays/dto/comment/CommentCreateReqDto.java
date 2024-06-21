package com.sparta.storyindays.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateReqDto {

    private String comment;

    public CommentCreateReqDto(String comment) {
        this.comment = comment;
    }
}
