package com.sparta.storyindays.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.storyindays.config.JwtConfig;
import com.sparta.storyindays.dto.ExceptionResDto;
import com.sparta.storyindays.enums.user.State;
import com.sparta.storyindays.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j(topic = "AuthenticationEntryPointImpl")
@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final JwtProvider jwtProvider;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException{
        log.info("인증 예외 처리");

        ExceptionResDto resDto;

        if(request.getAttribute(JwtConfig.EXPIRED_TOKEN) != null && (boolean) request.getAttribute(JwtConfig.EXPIRED_TOKEN)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
            resDto = new ExceptionResDto(HttpServletResponse.SC_UNAUTHORIZED, "만료된 accessToken 토큰입니다.");
        }else if(request.getAttribute(JwtConfig.BLOCKED_USER) != null && (boolean) request.getAttribute(JwtConfig.BLOCKED_USER)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); //403
            resDto = new ExceptionResDto(HttpServletResponse.SC_FORBIDDEN, "차단된 사용자입니다.");
        }else{
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); //403
            resDto = new ExceptionResDto(HttpServletResponse.SC_FORBIDDEN, "인증되지 않은 사용자입니다.");
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(resDto));
    }
}
