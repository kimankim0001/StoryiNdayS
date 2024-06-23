package com.sparta.storyindays.controller;


import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.dto.user.ProfileUpdateResDto;
import com.sparta.storyindays.security.UserDetailsImpl;
import com.sparta.storyindays.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follows/users/{userId}")
    public ResponseEntity<CommonResDto<Void>> followUser(@PathVariable long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {


        ProfileUpdateResDto updateResDto = followService.followUser(userId, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , updateResDto.getName() + "님을 팔로우하였습니다!"
                , null));
    }

    @PutMapping("/follows/users/{userId}")
    public ResponseEntity<CommonResDto<Void>> followCancle(@PathVariable long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ProfileUpdateResDto updateResDto = followService.followCancle(userId, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , updateResDto.getName() + "님의 팔로우를 취소하였습니다!"
                , null));
    }
}