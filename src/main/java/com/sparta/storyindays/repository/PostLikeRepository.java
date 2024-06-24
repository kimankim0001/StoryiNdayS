package com.sparta.storyindays.repository;

import com.sparta.storyindays.entity.PostLike;
import com.sparta.storyindays.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndUser(Long postId, User user);
}
