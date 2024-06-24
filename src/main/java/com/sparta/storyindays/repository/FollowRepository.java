package com.sparta.storyindays.repository;

import com.sparta.storyindays.entity.Follow;
import com.sparta.storyindays.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowUserIdAndFolloweeUser(String username, User user);

    List<Follow> findAllByFollowUserId(String userName);
}
