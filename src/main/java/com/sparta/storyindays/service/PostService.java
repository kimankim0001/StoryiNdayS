package com.sparta.storyindays.service;

import com.sparta.storyindays.config.JwtConfig;
import com.sparta.storyindays.dto.post.PostGetResDto;
import com.sparta.storyindays.dto.post.PostReqDto;
import com.sparta.storyindays.dto.post.PostResDto;
import com.sparta.storyindays.dto.post.PostUpdateResDto;
import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.enums.post.PostType;
import com.sparta.storyindays.exception.BusinessLogicException;
import com.sparta.storyindays.jwt.JwtProvider;
import com.sparta.storyindays.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public PostResDto writePost(PostReqDto reqDto, HttpServletRequest request, User user) {

        User curUser = validUserCheckAndGetUser(request,user);
        Post post = postRepository.save(reqDto.toPostEntity(curUser));
        PostResDto postReqDto = new PostResDto(post);

        return postReqDto;
    }

    public PostGetResDto getAllPost(int page, boolean isAsc) {

        Pageable pageable = getPageable(page, isAsc);

        // repository에서 공지글 찾아오기
        // repository에서 상단글 찾아오기
        // repository에서 일반글 찾아오기 (페이지), pageable
        PostGetResDto postGetResDto = new PostGetResDto(postRepository.findAllByPostType(PostType.NOTICE)
                , postRepository.findAllByPostTypeAndIsPinned(PostType.NORMAL, true)
                , postRepository.findAllByPostTypeAndIsPinned(PostType.NORMAL, false, pageable));

        postGetResDto.inputTestData();
        return postGetResDto;
    }

    public PostGetResDto getUserPost(String userName, int page, boolean isAsc) {
        Pageable pageable = getPageable(page, isAsc);

        User tempUser = userService.findByUserName(userName);

        PostGetResDto postGetResDto = new PostGetResDto(postRepository.findAllByPostType(PostType.NOTICE)
                , postRepository.findAllByPostTypeAndIsPinned(PostType.NORMAL, true)
                , postRepository.findAllByPostTypeAndIsPinnedAndUser(PostType.NORMAL, false, tempUser, pageable));

        postGetResDto.inputTestData();
        return postGetResDto;
    }

    @Transactional
    public PostUpdateResDto updatePost(long postId, PostReqDto reqDto, HttpServletRequest request, User user) {

        // 본인이 작성한 게시글의 수정인 경우만 인가하도록 조건걸기
        User curUser = validUserCheckAndGetUser(request,user);

        Post post = findById(postId);
        if(!post.getUser().getUsername().equals(curUser.getUsername()))
        {
            throw new BusinessLogicException("본인이 작성한 게시글만 수정할 수 있습니다");
        }

        post.update(reqDto);
        return new PostUpdateResDto(post);
    }

    public void deletePost(long postId, HttpServletRequest request, User user) {

        User curUser = validUserCheckAndGetUser(request,user);

        Post post = findById(postId);
        if(!post.getUser().getUsername().equals(curUser.getUsername()))
        {
            throw new BusinessLogicException("본인이 작성한 게시글만 삭제할 수 있습니다");
        }
        postRepository.delete(post);
    }

    public Post findById(long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시글 입니다"));
    }

    public Pageable getPageable(int page, boolean isAsc) {
        // 정렬방향, 정렬 기준(생성일자 고정), 페이저블 생성
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "createdAt");
        Pageable pageable = PageRequest.of(page, 5, sort);

        return pageable;
    }

    public User validUserCheckAndGetUser (HttpServletRequest request, User user) {
        // 유효한 JWT 토큰을 체크 jwtProvider
        String accessToken = request.getHeader(JwtConfig.AUTHORIZATION_HEADER);
        accessToken = jwtProvider.substringToken(accessToken);
        jwtProvider.isTokenValidate(accessToken, request);
        String jwtTokenUserName = jwtProvider.getUserInfoFromToken(accessToken).getSubject();
        // 입력받은 유저를 레포지토리에서 찾아옴
        User inputUser = userService.findById(user.getId());

        // jwt토큰의 유저정보와 입력받은 유저정보 비교.. 구지 필요한지 고민
        if(!jwtTokenUserName.equals(inputUser.getUsername())){
            throw new IllegalArgumentException("유효한 유저가 아닙니다");
        }
        return inputUser;
    }
}
