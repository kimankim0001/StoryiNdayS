package com.sparta.storyindays.security;

import com.sparta.storyindays.config.JwtConfig;
import com.sparta.storyindays.enums.user.State;
import com.sparta.storyindays.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Slf4j(topic = "jwtAuthenticationFilter")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtProvider.getJwtFromHeader(req, JwtConfig.AUTHORIZATION_HEADER);

        if(!StringUtils.hasText(token)){
            log.info("토큰 아예 없는 경우");
            filterChain.doFilter(req,res);
            return;
        }

        token = jwtProvider.substringToken(token);

        if(!jwtProvider.isTokenValidate(token,req) ){
            log.info("정상 토큰 없는 상태");
            filterChain.doFilter(req,res);
            return;
        }

        log.info("정상 토큰 있는 상태");
        Claims userInfo = jwtProvider.getUserInfoFromToken(token);

        String userState = userInfo.get(JwtConfig.USER_STATE_KEY,String.class);

        if(State.BLOCK.getState().equals(userState)){
            log.info("차단된 사용자");
            throw new AccessDeniedException("차단되어서 해당 사이트에 접근하실 수 없습니다.");
        }

        setAuthentication(userInfo.getSubject());

        filterChain.doFilter(req, res);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
