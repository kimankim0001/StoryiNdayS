package com.sparta.storyindays.controller;

import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.dto.user.ProfileResDto;
import com.sparta.storyindays.dto.user.ProfileUpdateReqDto;
import com.sparta.storyindays.dto.user.ProfileUpdateResDto;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<CommonResDto<ProfileResDto>> getProfile(@PathVariable Long userId) {
        ProfileResDto responseDto = userService.getProfile(userId);
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , "프로필 조회에 성공하였습니다!"
                , responseDto));
    }

    @PutMapping("/users/{userId}/profile")
    public ResponseEntity<CommonResDto<ProfileUpdateResDto>> updateProfile(@PathVariable Long userId, @RequestBody @Valid ProfileUpdateReqDto reqDto) {
        ProfileUpdateResDto responseDto = userService.updateProfile(userId, reqDto);
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
            ,"프로필 수정에 성공하였습니다!"
            , responseDto));
    }
}
