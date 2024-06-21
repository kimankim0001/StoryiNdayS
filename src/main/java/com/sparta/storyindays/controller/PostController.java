package com.sparta.storyindays.controller;

import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.dto.post.*;
import com.sparta.storyindays.security.UserDetailsImpl;
import com.sparta.storyindays.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "컨트롤러단")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping("/posts")
    public ResponseEntity<CommonResDto<PostResDto>> writePost(@RequestBody PostReqDto reqDto,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostResDto resDto = postService.writePost(reqDto, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , "게시글 작성에 성공했습니다!"
                , resDto));
    }

    @GetMapping("/posts")
    public ResponseEntity<CommonResDto<PostGetResDto>> getAllPost(@RequestParam("page") int page
            , @RequestParam("isAsc") boolean isAsc) {

        PostGetResDto updateResDtoList = postService.getAllPost(page - 1, isAsc);
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , "전체 게시글 조회에 성공했습니다!"
                , updateResDtoList));
    }

    @GetMapping("/posts/users")
    public ResponseEntity<CommonResDto<PostGetResDto>> getUserPost(
            @RequestParam("userName") String userName
            , @RequestParam("page") int page
            , @RequestParam("isAsc") boolean isAsc) {

        PostGetResDto updateResDtoList = postService.getUserPost(userName, page - 1, isAsc);
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , userName + "의 게시글 조회에 성공했습니다!"
                , updateResDtoList));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<CommonResDto<PostUpdateResDto>> updatePost(@PathVariable long postId, @RequestBody PostReqDto reqDto
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostUpdateResDto updateResDto = postService.updatePost(postId, reqDto, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , "게시글 수정에 성공했습니다!"
                , updateResDto));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<CommonResDto<Void>> deletePost(@PathVariable long postId
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.deletePost(postId, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , "게시글 삭제에 성공했습니다!",
                null));
    }

    @PostMapping("/admins/posts")
    public ResponseEntity<CommonResDto<PostNotifyResDto>> writeNoticePost(@RequestBody PostReqDto reqDto,
                                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info("컨트롤러 공지등록");
        PostNotifyResDto resDto = postService.writeNoticePost(reqDto, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , "공지 등록에 성공했습니다!"
                , resDto));
    }

    @PutMapping("/admins/posts/{postId}")
    public ResponseEntity<CommonResDto<PostUpdateResDto>> updatePostByAdmin(@PathVariable long postId, @RequestBody PostReqDto reqDto
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostUpdateResDto updateResDto = postService.updatePostByAdmin(postId, reqDto, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , "게시글 수정에 성공했습니다!"
                , updateResDto));
    }

    @DeleteMapping("/admins/posts/{postId}")
    public ResponseEntity<CommonResDto<Void>> deletePostByAdmin(@PathVariable long postId
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.deletePostByAdmin(postId, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , "게시글 삭제에 성공했습니다!",
                null));
    }

    @PutMapping("/admins/posts/{postId}/pins")
    public ResponseEntity<CommonResDto<PostUpdateResDto>> pinPost(@PathVariable long postId
            , @RequestParam("isPinned") boolean isPinned
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostUpdateResDto updateResDto = postService.pinPost(postId, isPinned, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , "게시글 고정에 성공했습니다"
                , updateResDto));
    }
}
