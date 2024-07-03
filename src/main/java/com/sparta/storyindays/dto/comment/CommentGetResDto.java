package com.sparta.storyindays.dto.comment;

import com.sparta.storyindays.entity.Comment;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class CommentGetResDto {

    Page<CommentResDto> commentList;

    public CommentGetResDto(Page<Comment> commentList) {
        this.commentList = commentList.map(CommentResDto::new);
    }
}
