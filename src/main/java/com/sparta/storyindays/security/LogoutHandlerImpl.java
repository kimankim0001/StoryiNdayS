package com.sparta.storyindays.security;

import com.sparta.storyindays.config.JwtConfig;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.exception.BusinessLogicException;
import com.sparta.storyindays.jwt.JwtProvider;
import com.sparta.storyindays.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "LogoutHandlerImpl")
@Component
@RequiredArgsConstructor
public class LogoutHandlerImpl implements LogoutHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final LogoutExceptionHandler logoutExceptionHandler;

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("로그아웃 시도");


        String token = jwtProvider.getJwtFromHeader(request, JwtConfig.AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(token)) {
            throw new BusinessLogicException("토큰이 유효하지 않습니다.");
        }

        token = jwtProvider.substringToken(token);
        Claims userInfo = jwtProvider.getUserInfoFromToken(token);

        User user = userRepository.findByUsername(userInfo.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        user.updateRefreshToken("");
        userRepository.save(user);

    }

}
