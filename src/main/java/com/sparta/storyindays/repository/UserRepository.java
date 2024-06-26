package com.sparta.storyindays.repository;

import com.sparta.storyindays.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Page<User> findAll(Pageable pageable);
    Optional<User> findByKakaoId(Long kakaoId);
    Optional<User> findByEmail(String kakaoEmail);
}
