package com.sparta.storyindays.repository;

import com.sparta.storyindays.entity.Comment;
import com.sparta.storyindays.entity.User;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> getLikesCommentWithPageAndSortDesc(User user, long offset, int pageSize);
}
