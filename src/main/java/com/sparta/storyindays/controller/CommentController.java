package com.sparta.storyindays.controller;

import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.dto.comment.CommentCreateReqDto;
import com.sparta.storyindays.security.UserDetailsImpl;
import com.sparta.storyindays.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResDto<CommonResDto>> createComment(@PathVariable(name = "postId") long postId, @Valid @RequestBody CommentCreateReqDto reqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommonResDto resDto = commentService.createComment(postId, reqDto, userDetails.getUser);
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "댓글 작성에 성공하였습니다!", resDto));
    }

}
