package com.sparta.storyindays.controller;

import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.dto.comment.CommentCreateReqDto;
import com.sparta.storyindays.dto.comment.CommentGetResDto;
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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResDto<CommentResDto>> createComment(@PathVariable(name = "postId") long postId, @Valid @RequestBody CommentCreateReqDto reqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResDto resDto = commentService.createComment(postId, reqDto, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "댓글 작성에 성공하였습니다!", resDto));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResDto<CommentGetResDto>> getAllComment(@PathVariable(name = "postId") long postId, @RequestParam("page") int page, @RequestParam("isAsc") boolean isAsc) {
        CommentGetResDto commentGetResDto = commentService.getAllComment(postId, page - 1, isAsc);
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "댓글 조회에 성공하였습니다!", commentGetResDto));
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResDto<CommentResDto>> updateComment(@PathVariable(name = "postId") long postId, @PathVariable(name = "commentId") long commentId, @Valid @RequestBody CommentUpdateReqDto reqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResDto resDto = commentService.updateComment(postId, commentId, reqDto, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "댓글 수정에 성공하였습니다!", resDto));
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResDto<Void>> deleteComment(@PathVariable(name = "postId") long postId, @PathVariable(name = "commentId") long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(postId, commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "댓글 삭제에 성공하였습니다!", null));
    }

    @PutMapping("/admins/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResDto<CommentResDto>> updateCommentAdmin(@PathVariable(name = "postId") long postId, @PathVariable(name = "commentId") long commentId, @Valid @RequestBody CommentUpdateReqDto reqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResDto resDto = commentService.updateCommentAdmin(postId, commentId, reqDto, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "관리자 권한으로 댓글 수정에 성공하였습니다!", resDto));
    }

    @DeleteMapping("/admins/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResDto<Void>> deleteCommentAdmin(@PathVariable(name = "postId") long postId, @PathVariable(name = "commentId") long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteCommentAdmin(postId, commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "관리자 권한으로 댓글 삭제에 성공하였습니다!", null));
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResDto<CommentResDto>> getComment(@PathVariable(name = "postId") long postId, @PathVariable(name = "commentId") long commentId) {
        CommentResDto resDto = commentService.getComment(postId, commentId);
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value(), "댓글 조회에 성공하였습니다!", resDto));
    }
}
