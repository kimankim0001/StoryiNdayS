package com.sparta.storyindays.repository;

import com.sparta.storyindays.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
