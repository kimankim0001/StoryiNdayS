package com.sparta.storyindays.repository;

import com.sparta.storyindays.entity.PasswordHistory;
import com.sparta.storyindays.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    List<PasswordHistory> findTop4ByUserOrderByCreatedAtDesc(User user);
    List<PasswordHistory> findAllByUserOrderByCreatedAtAsc(User user);
}
