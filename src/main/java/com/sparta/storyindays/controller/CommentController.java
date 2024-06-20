package com.sparta.storyindays.controller;

import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.dto.comment.CommentCreateReqDto;
import com.sparta.storyindays.dto.comment.CommentResDto;
import com.sparta.storyindays.dto.comment.CommentUpdateReqDto;
import com.sparta.storyindays.security.UserDetailsImpl;
import com.sparta.storyindays.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResDto<CommentResDto>> createComment(@PathVariable(name = "postId") long postId, @Valid @RequestBody CommentCreateReqDto reqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResDto resDto = commentService.createComment(postId, reqDto, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "댓글 작성에 성공하였습니다!", resDto));
    }

    //댓글 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResDto<List<CommentResDto>>> getAllComment (@PathVariable(name = "postId") long postId) {
        List<CommentResDto> resDto = commentService.getAllComment(postId);
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "댓글 조회에 성공하였습니다!", resDto));
    }

    // 댓글 수정
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResDto<CommentResDto>> updateComment (@PathVariable(name = "postId") long postId, @PathVariable(name = "commentId") long commentId, @Valid @RequestBody CommentUpdateReqDto reqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResDto resDto = commentService.updateComment(postId, commentId, reqDto, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "댓글 수정에 성공하였습니다!", resDto));
    }
}
