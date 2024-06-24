package com.sparta.storyindays.controller;

import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.security.UserDetailsImpl;
import com.sparta.storyindays.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("/posts/{postId}/comments/{commentId}/likes")
    public ResponseEntity<CommonResDto<Void>> addCommentLike(@PathVariable(name = "postId") long postId, @PathVariable(name = "commentId") long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentLikeService.addCommentLike(postId, commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "댓글 좋아요 상태로 변경되었습니다.", null));
    }

    @PutMapping("/posts/{postId}/comments/{commentId}/likes")
    public ResponseEntity<CommonResDto<Void>> cancelCommentLike(@PathVariable(name = "postId") long postId, @PathVariable(name = "commentId") long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentLikeService.cancelCommentLike(postId, commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "댓글 좋아요 상태가 취소되었습니다.", null));
    }
}
