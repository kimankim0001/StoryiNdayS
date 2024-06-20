package com.sparta.storyindays.controller;

import com.sparta.storyindays.dto.CommonResDto;
import com.sparta.storyindays.dto.post.PostReqDto;
import com.sparta.storyindays.dto.post.PostResDto;
import com.sparta.storyindays.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
