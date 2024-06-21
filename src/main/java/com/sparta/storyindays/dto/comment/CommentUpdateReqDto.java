package com.sparta.storyindays.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateReqDto {

    private String comment;

    public CommentUpdateReqDto(String comment) {
        this.comment = comment;
    }
}
