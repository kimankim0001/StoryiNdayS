package com.sparta.storyindays.repository;

import com.sparta.storyindays.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    Page<Comment> findAllByPostId(long postId, Pageable pageable);
    List<Comment> findAllByPostId(long postId);
}
