package com.sparta.storyindays.repository;

import com.sparta.storyindays.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
