package com.sparta.storyindays.controller;

import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.dto.post.PostGetResDto;
import com.sparta.storyindays.dto.post.PostReqDto;
import com.sparta.storyindays.dto.post.PostResDto;
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
    public ResponseEntity<CommonResDto<PostResDto>> writePost(@RequestBody PostReqDto reqDto, HttpServletRequest request) {

        //String accessToken = request.getHeader("Authorization").substring(7);
        String accessToken = "";
        PostResDto resDto = postService.writePost(reqDto, accessToken);
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

//    @GetMapping("/users")
//    public ResponseEntity<CommonResDto<PostGetResDto>> getUserPost(@AuthenticationPrincipal UserDetailsImpl userDetails
//            , @RequestParam("page") int page
//            , @RequestParam("isAsc") boolean isAsc) {
//
//        PostGetResDto updateResDtoList = postService.getUserPost(userDetails.getUser(), page - 1, isAsc);
//        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK.value()
//                , "전체 게시물 조회에 성공했습니다!"
//                , updateResDtoList));
//    }
}
