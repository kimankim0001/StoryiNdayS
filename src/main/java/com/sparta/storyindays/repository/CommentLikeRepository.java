package com.sparta.storyindays.repository;

import com.sparta.storyindays.entity.CommentLike;
import com.sparta.storyindays.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByPostIdAndCommentIdAndUser(Long postId, Long commentId,User user);
}
