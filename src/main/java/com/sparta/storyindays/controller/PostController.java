package com.sparta.storyindays.controller;

import com.sparta.storyindays.config.JwtConfig;
import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.dto.post.PostGetResDto;
import com.sparta.storyindays.dto.post.PostReqDto;
import com.sparta.storyindays.dto.post.PostResDto;
import com.sparta.storyindays.dto.post.PostUpdateResDto;
import com.sparta.storyindays.security.UserDetailsImpl;
import com.sparta.storyindays.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<CommonResDto<PostResDto>> writePost(@RequestBody PostReqDto reqDto, HttpServletRequest request,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {


        PostResDto resDto = postService.writePost(reqDto, request, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , "게시물 작성에 성공했습니다!"
                , resDto));
    }

    @GetMapping
    public ResponseEntity<CommonResDto<PostGetResDto>> getAllPost(@RequestParam("page") int page
            , @RequestParam("isAsc") boolean isAsc) {

        PostGetResDto updateResDtoList = postService.getAllPost(page - 1, isAsc);
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , "전체 게시물 조회에 성공했습니다!"
                , updateResDtoList));
    }

    @GetMapping("/users")
    public ResponseEntity<CommonResDto<PostGetResDto>> getUserPost(@AuthenticationPrincipal UserDetailsImpl userDetails
            , @RequestParam("page") int page
            , @RequestParam("isAsc") boolean isAsc) {

        PostGetResDto updateResDtoList = postService.getUserPost(userDetails.getUser(), page - 1, isAsc);
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , userDetails.getUsername() + "의 게시물 조회에 성공했습니다!"
                , updateResDtoList));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<CommonResDto<PostUpdateResDto>> updatePost(@PathVariable long postId, @RequestBody PostReqDto reqDto) {

        PostUpdateResDto updateResDto = postService.updatePost(postId, reqDto);
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , "게시물 수정에 성공했습니다!"
                , updateResDto));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResDto<Void>> deletePost(@PathVariable long postId){

        postService.deletePost(postId);
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
                , "게시물 삭제에 성공했습니다!" ,
                null));
    }
}
