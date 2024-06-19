package com.sparta.storyindays.controller;

import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.dto.user.SignupReqDto;
import com.sparta.storyindays.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/users/signup")
    public ResponseEntity<CommonResDto> signup(@Valid @RequestBody SignupReqDto signupReqDto) {
        authService.signup(signupReqDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK.value(), "회원가입이 완료되었습니다!", null);
        return ResponseEntity.ok().body(resDto);
    }
}
