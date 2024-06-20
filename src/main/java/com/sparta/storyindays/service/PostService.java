package com.sparta.storyindays.service;

import com.sparta.storyindays.dto.post.PostReqDto;
import com.sparta.storyindays.dto.post.PostResDto;
import com.sparta.storyindays.dto.user.Auth;
import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.jwt.JwtProvider;
import com.sparta.storyindays.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final JwtProvider jwtProvider;

    public PostResDto writePost(PostReqDto reqDto, String accessToken) {
        // 유효한 JWT 토큰을 가진 본인, 인가된 유저인지 체크 jwtProvider

        // 유저를 레포지토리에서 찾아옴
        User user = new User("test","123","lee", Auth.USER,"test@email.com");
        // req, 찾아온 user로 게시글 entity 생성
        Post post = postRepository.save(reqDto.toPostEntity(user));
        // save 후 res dto로 반환
        PostResDto postReqDto = new PostResDto(post);

        return postReqDto;
    }
}
