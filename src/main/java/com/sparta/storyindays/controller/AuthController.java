package com.sparta.storyindays.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.storyindays.config.JwtConfig;
import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.dto.user.LoginReqDto;
import com.sparta.storyindays.dto.user.SignupReqDto;
import com.sparta.storyindays.jwt.JwtProvider;
import com.sparta.storyindays.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "AuthController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/users/signup")
    public ResponseEntity<CommonResDto<Void>> signup(@Valid @RequestBody SignupReqDto signupReqDto) {
        authService.signup(signupReqDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK.value(), "회원가입이 완료되었습니다!", null);
        return ResponseEntity.ok().body(resDto);
    }

    @PostMapping("/users/login")
    public ResponseEntity<CommonResDto<Void>> login(@Valid @RequestBody LoginReqDto loginReqDto, HttpServletResponse response) {
        List<String> tokens = authService.login(loginReqDto);
        response.addHeader(JwtConfig.ACCESS_TOKEN_HEADER, tokens.get(0));
        response.addHeader(JwtConfig.REFRESH_TOKEN_HEADER,tokens.get(1));

        CommonResDto resDto = new CommonResDto(HttpStatus.OK.value(), "로그인이 완료되었습니다 !", null);
        return ResponseEntity.ok().body(resDto);
    }

    @PostMapping("/users/reissuance")
    public ResponseEntity<CommonResDto<Void>> reissue(HttpServletRequest request,HttpServletResponse response) {
        String refreshToken = jwtProvider.getJwtFromHeader(request,JwtConfig.AUTHORIZATION_HEADER);
        String accessToken = authService.reissue(refreshToken);
        response.addHeader(JwtConfig.AUTHORIZATION_HEADER, accessToken);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK.value(), "토큰 재발행이 완료되었습니다 !", null);
        return ResponseEntity.ok().body(resDto);
    }

    @PostMapping("/users/login/kakao")
    public ResponseEntity<CommonResDto<Void>> kakaoLogin(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        log.info("카카오 토큰 반환 완료");
        String kakaoAccessToken = jwtProvider.getJwtFromHeader(request,JwtConfig.KAKAO_TOKEN_HEADER);
        List<String> tokens = authService.kakaoLogin(kakaoAccessToken);
        response.addHeader(JwtConfig.ACCESS_TOKEN_HEADER, tokens.get(0));
        response.addHeader(JwtConfig.REFRESH_TOKEN_HEADER,tokens.get(1));

        CommonResDto resDto = new CommonResDto(HttpStatus.OK.value(), "로그인이 완료되었습니다 !", null);
        return ResponseEntity.ok().body(resDto);
    }
}
