package com.sparta.storyindays.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.storyindays.config.JwtConfig;
import com.sparta.storyindays.dto.ExceptionResDto;
import com.sparta.storyindays.enums.user.Auth;
import com.sparta.storyindays.enums.user.State;
import com.sparta.storyindays.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j(topic = "AccessDeniedHandlerImpl")
@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("AccessDeniedHandlerImpl 예외처리");

        String accessToken = jwtProvider.getJwtFromHeader(request, JwtConfig.AUTHORIZATION_HEADER);

        ExceptionResDto resDto = null;

        if (!StringUtils.hasText(accessToken)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); //403
            resDto = new ExceptionResDto(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
        }

        accessToken = jwtProvider.substringToken(accessToken);
        Claims claims = jwtProvider.getUserInfoFromToken(accessToken);

        String auth = claims.get(JwtConfig.AUTHORIZATION_KEY, String.class);

        if(Auth.USER.getUser().equals(auth)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resDto = new ExceptionResDto(HttpServletResponse.SC_FORBIDDEN, "ADMIN만 해당 페이지에 접근할 수 있습니다.");
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(resDto));

        SecurityContextHolder.clearContext();
    }
}
