package com.sparta.storyindays.service;

import com.sparta.storyindays.dto.user.*;
import com.sparta.storyindays.dto.user.admin.*;
import com.sparta.storyindays.entity.PasswordHistory;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.enums.user.Auth;
import com.sparta.storyindays.enums.user.State;
import com.sparta.storyindays.repository.PasswordHistoryRepository;
import com.sparta.storyindays.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final MessageSource messageSource;

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
            throw new IllegalArgumentException(messageSource.getMessage(
                    "only.myacount.can.change.profile",
                    null,
                    "You can only modify the profile of your account.",
                    Locale.getDefault()
            ));
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
        User currentUser = getCurrentUser();
        String currentPassword = passwordUpdateReqDto.getCurrentPassword();
        String newPassword = passwordUpdateReqDto.getNewPassword();

        // 현재 로그인한 계정이 맞는지 검증
        if (!currentUser.getId().equals(userId)) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "only.myacount.can.change.password",
                    null,
                    "You can only change your password.",
                    Locale.getDefault()
            ));
        }

        // 현재 비밀번호가 사용자의 비밀번호와 맞는지 검증
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "no.match.user.password",
                    null,
                    "The current password and the user's password do not match.",
                    Locale.getDefault()
            ));
        }

        // 변경할 비밀번호와 현재 비밀번호가 동일한지 검증
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "current.and.new.password.same",
                    null,
                    "You cannot change it with the same password.",
                    Locale.getDefault()
            ));
        }

        // 변경할 비밀번호가 최근에 바꾼 적 있는 비밀번호인지 검증
        List<PasswordHistory> recentPasswords = passwordHistoryRepository.findTop4ByUserOrderByCreatedAtDesc(user);
        for (PasswordHistory passwordHistory : recentPasswords) {
            if (passwordEncoder.matches(passwordUpdateReqDto.getNewPassword(), passwordHistory.getPassword())) {
                throw new IllegalArgumentException(messageSource.getMessage(
                        "new.and.recent.password.same",
                        null,
                        "The new password cannot be the same as the password you recently used.",
                        Locale.getDefault()
                ));
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
                new IllegalArgumentException(messageSource.getMessage(
                        "no.exist.user",
                        null,
                        "This user does not exist.",
                        Locale.getDefault()
                ))
        );
    }

    public User findByUserName(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(() ->
                new IllegalArgumentException(messageSource.getMessage(
                        "no.exist.user",
                        null,
                        "This user does not exist.",
                        Locale.getDefault()
                )));
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage(
                        "not.found.current.user",
                        null,
                        "The current user could not be found.",
                        Locale.getDefault()
                )));
    }

    public Page<AdminUsersResDto> getAllUserProfile(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction,sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> userList;
        userList = userRepository.findAll(pageable);

        return userList.map(AdminUsersResDto::new);
    }
}
