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

        String accessToken = jwtProvider.getJwtFromHeader(request, JwtConfig.AUTHORIZATION_HEADER);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN); //403
        ExceptionResDto resDto = new ExceptionResDto(HttpServletResponse.SC_FORBIDDEN, authException.getMessage());

        if (!StringUtils.hasText(accessToken)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); //403
            resDto = new ExceptionResDto(HttpServletResponse.SC_FORBIDDEN, authException.getMessage());
        }

        accessToken = jwtProvider.substringToken(accessToken);
        Claims claims = jwtProvider.getUserInfoFromToken(accessToken);
        String state = claims.get(JwtConfig.USER_STATE_KEY, String.class);

        if (State.BLOCK.getState().equals(state)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); //403
            resDto = new ExceptionResDto(HttpServletResponse.SC_FORBIDDEN, "차단되어서 해당 사이트에 접근하실 수 없습니다.");
        }

        if(request.getAttribute(JwtConfig.EXPIRED_TOKEN) != null && (boolean) request.getAttribute(JwtConfig.EXPIRED_TOKEN)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
            resDto = new ExceptionResDto(HttpServletResponse.SC_UNAUTHORIZED, "만료된 accessToken 토큰입니다.");
        }


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(resDto));
    }
}
