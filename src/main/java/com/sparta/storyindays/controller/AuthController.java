package com.sparta.storyindays.controller;

import com.sparta.storyindays.config.JwtConfig;
import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.dto.user.LoginReqDto;
import com.sparta.storyindays.dto.user.SignupReqDto;
import com.sparta.storyindays.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/users/signup")
    public ResponseEntity<CommonResDto<Void>> signup(@Valid @RequestBody SignupReqDto signupReqDto) {
        authService.signup(signupReqDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK.value(), "회원가입이 완료되었습니다!", null);
        return ResponseEntity.ok().body(resDto);
    }

    @PostMapping("/users/login")
    public ResponseEntity<CommonResDto<Void>> login(@Valid @RequestBody LoginReqDto loginReqDto, HttpServletResponse response) {
        String accessToken = authService.login(loginReqDto);
        response.addHeader(JwtConfig.AUTHORIZATION_HEADER, accessToken);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK.value(), "로그인이 완료되었습니다 !", null);
        return ResponseEntity.ok().body(resDto);
    }
}