package com.sparta.storyindays.controller;

import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.security.UserDetailsImpl;
import com.sparta.storyindays.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<CommonResDto<Void>> addPostLike(@PathVariable(name = "postId") long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postLikeService.addPostLike(postId, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "게시글 좋아요 상태로 변경되었습니다.", null));
    }

    @PutMapping("/posts/{postId}/likes")
    public ResponseEntity<CommonResDto<Void>> cancelPostLike(@PathVariable(name = "postId") long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postLikeService.cancelPostLike(postId, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "게시글 좋아요 상태가 취소되었습니다.", null));
    }
}
