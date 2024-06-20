package com.sparta.storyindays.jwt;

import com.sparta.storyindays.config.JwtConfig;
import com.sparta.storyindays.dto.user.Auth;
import com.sparta.storyindays.dto.user.State;
import com.sparta.storyindays.entity.User;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j(topic = "JwtProvider")
@Component
public class JwtProvider {

    public String createToken(User user, String tokenType) {
        Date date = new Date();

        String username = user.getUsername();
        State state = user.getState();
        Auth auth = user.getAuth();

        if("accessToken".equals(tokenType)){
            return JwtConfig.BEARER_PREFIX +
                    Jwts.builder()
                            .setSubject(username)
                            .setExpiration(new Date(date.getTime() + JwtConfig.accessTokenTime))
                            .claim(JwtConfig.AUTHORIZATION_KEY, auth)
                            .claim(JwtConfig.USER_STATE_KEY, state)
                            .setIssuedAt(date)
                            .signWith(JwtConfig.key, JwtConfig.signatureAlgorithm)
                            .compact();

        } else if ("refreshToken".equals(tokenType)) {
            return JwtConfig.BEARER_PREFIX +
                    Jwts.builder()
                            .setSubject(username)
                            .setExpiration(new Date(date.getTime() + JwtConfig.refreshTokenTime))
                            .claim(JwtConfig.AUTHORIZATION_KEY, auth)
                            .claim(JwtConfig.USER_STATE_KEY, state)
                            .setIssuedAt(date)
                            .signWith(JwtConfig.key, JwtConfig.signatureAlgorithm)
                            .compact();
        } else{
            return null;
        }
    }
}
