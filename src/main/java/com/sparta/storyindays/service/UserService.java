package com.sparta.storyindays.service;

import com.sparta.storyindays.dto.user.*;
import com.sparta.storyindays.entity.PasswordHistory;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.repository.PasswordHistoryRepository;
import com.sparta.storyindays.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHistoryRepository passwordHistoryRepository;

    public ProfileResDto getProfile(Long userId) {
        User user = findById(userId);
        ProfileResDto profileResDto = new ProfileResDto(user);
        return profileResDto;
    }

    @Transactional
    public ProfileUpdateResDto updateProfile(Long userId, ProfileUpdateReqDto profileUpdateReqDto) {
        User user = findById(userId);
        user.update(profileUpdateReqDto);
        return new ProfileUpdateResDto(user);
    }

    @Transactional
    public void updatePassword(Long userId, PasswordUpdateReqDto passwordUpdateReqDto) {
        User user = findById(userId);
        String currentPassword = passwordUpdateReqDto.getCurrentPassword();
        String newPassword = passwordUpdateReqDto.getNewPassword();

        // 현재 비밀번호가 사용자의 비밀번호와 맞는지 검증
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호와 사용자의 비밀번호가 일치하지 않습니다.");
        }

        // 변경할 비밀번호와 현재 비밀번호가 동일한지 검증
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("동일한 비밀번호로는 변경할 수 없습니다.");
        }

        // 변경할 비밀번호가 최근에 바꾼 적 있는 비밀번호인지 검증
        List<PasswordHistory> recentPasswords = passwordHistoryRepository.findTop3ByUserOrderByChangedAtDesc(user);
        for (PasswordHistory passwordHistory : recentPasswords) {
            if (passwordEncoder.matches(passwordUpdateReqDto.getNewPassword(), passwordHistory.getPassword())) {
                throw new IllegalArgumentException("새로운 비밀번호는 최근 사용한 비밀번호와 동일할 수 없습니다.");
            }
        }

        // 변경할 비밀번호로 수정
        user.passwordUpdate(passwordEncoder.encode(newPassword));
        PasswordHistory newHistory = new PasswordHistory();
        newHistory.setUser(user);
        newHistory.setPassword(user.getPassword());
        passwordHistoryRepository.save(newHistory);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자는 존재하지 않습니다.")
        );
    }
}
