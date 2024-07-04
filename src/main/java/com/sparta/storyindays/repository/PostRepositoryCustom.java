package com.sparta.storyindays.repository;

import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.entity.User;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getLikesPostWithPageAndSortDesc(User user, long offset, int pageSize);
}
