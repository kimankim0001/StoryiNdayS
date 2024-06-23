package com.sparta.storyindays.service;

import com.sparta.storyindays.dto.user.*;
import com.sparta.storyindays.dto.user.admin.AdminAuthReqDto;
import com.sparta.storyindays.dto.user.admin.AdminAuthResDto;
import com.sparta.storyindays.dto.user.admin.AdminStateReqDto;
import com.sparta.storyindays.dto.user.admin.AdminStateResDto;
import com.sparta.storyindays.entity.PasswordHistory;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.enums.user.Auth;
import com.sparta.storyindays.enums.user.State;
import com.sparta.storyindays.exception.BusinessLogicException;
import com.sparta.storyindays.repository.PasswordHistoryRepository;
import com.sparta.storyindays.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHistoryRepository passwordHistoryRepository;

    // 프로필 조회
    public ProfileResDto getProfile(Long userId) {
        User user = findById(userId);
        ProfileResDto profileResDto = new ProfileResDto(user);
        return profileResDto;
    }

    // 프로필 수정
    @Transactional
    public ProfileUpdateResDto updateProfile(Long userId, ProfileUpdateReqDto profileUpdateReqDto) {
        User currentUser = getCurrentUser();
        if (!currentUser.getId().equals(userId)) {
            throw new IllegalArgumentException("본인의 프로필만 수정할 수 있습니다.");
        }

        User user = findById(userId);
        user.update(profileUpdateReqDto);
        return new ProfileUpdateResDto(user);
    }

    // 회원 정보 수정
    @Transactional
    public ProfileUpdateResDto adminUpdateProfile(Long userId, ProfileUpdateReqDto profileUpdateReqDto) {
        User user = findById(userId);
        user.update(profileUpdateReqDto);
        return new ProfileUpdateResDto(user);
    }

    // 권한 변경
    @Transactional
    public AdminAuthResDto updateAuth(Long userId, AdminAuthReqDto adminAuthReqDto) {
        User user = findById(userId);
        Auth auth = adminAuthReqDto.getAuth();
        user.authUpdate(auth);
        return new AdminAuthResDto(user);
    }

    // 상태 변경
    @Transactional
    public AdminStateResDto updateState(Long userId, AdminStateReqDto adminStateReqDto) {
        User user = findById(userId);
        State state = adminStateReqDto.getState();
        user.stateUpdate(state);
        return new AdminStateResDto(user);
    }

    // 비밀번호 변경
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
        List<PasswordHistory> recentPasswords = passwordHistoryRepository.findTop4ByUserOrderByCreatedAtDesc(user);
        for (PasswordHistory passwordHistory : recentPasswords) {
            if (passwordEncoder.matches(passwordUpdateReqDto.getNewPassword(), passwordHistory.getPassword())) {
                throw new IllegalArgumentException("새로운 비밀번호는 최근 사용한 비밀번호와 동일할 수 없습니다.");
            }
        }

        // 변경할 비밀번호로 수정
        user.passwordUpdate(passwordEncoder.encode(newPassword));

        // 변경한 비밀번호 히스토리에 저장
        PasswordHistory newHistory = new PasswordHistory();
        newHistory.setUser(user);
        newHistory.setPassword(user.getPassword());
        passwordHistoryRepository.save(newHistory);

        // 비밀번호 히스토리가 4개를 초과할 경우 가장 오래된 히스토리 삭제
        List<PasswordHistory> allPasswords = passwordHistoryRepository.findAllByUserOrderByCreatedAtAsc(user);
        if (allPasswords.size() > 4) {
            PasswordHistory oldestHistory = allPasswords.get(0);
            passwordHistoryRepository.delete(oldestHistory);
        }
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자는 존재하지 않습니다.")
        );
    }

    public User findByUserName(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자는 존재하지 않습니다"));
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("현재 사용자를 찾을 수 없습니다."));
    }
}
