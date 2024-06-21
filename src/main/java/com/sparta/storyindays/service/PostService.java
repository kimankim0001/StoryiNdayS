package com.sparta.storyindays.service;

import com.sparta.storyindays.dto.post.*;
import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.enums.post.PostType;
import com.sparta.storyindays.enums.user.Auth;
import com.sparta.storyindays.exception.BusinessLogicException;
import com.sparta.storyindays.repository.PostRepository;
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

    public PostResDto writePost(PostReqDto reqDto, User user) {

        User curUser = userService.findById(user.getId());
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

        return postGetResDto;
    }

    public PostGetResDto getUserPost(String userName, int page, boolean isAsc) {
        Pageable pageable = getPageable(page, isAsc);

        User searchUser = userService.findByUserName(userName);

        PostGetResDto postGetResDto = new PostGetResDto(postRepository.findAllByPostType(PostType.NOTICE)
                , postRepository.findAllByPostTypeAndIsPinned(PostType.NORMAL, true)
                , postRepository.findAllByPostTypeAndIsPinnedAndUser(PostType.NORMAL, false, searchUser, pageable));

        return postGetResDto;
    }

    @Transactional
    public PostUpdateResDto updatePost(long postId, PostReqDto reqDto, User user) {

        User curUser = userService.findById(user.getId());

        Post post = findById(postId);
        if(!post.getUser().getUsername().equals(curUser.getUsername()))
        {
            throw new BusinessLogicException("본인이 작성한 게시글만 수정할 수 있습니다");
        }

        post.update(reqDto);
        return new PostUpdateResDto(post);
    }

    public void deletePost(long postId, User user) {

        User curUser = userService.findById(user.getId());

        Post post = findById(postId);
        if(!post.getUser().getUsername().equals(curUser.getUsername()))
        {
            throw new BusinessLogicException("본인이 작성한 게시글만 삭제할 수 있습니다");
        }
        postRepository.delete(post);
    }


    public PostNotifyResDto writeNoticePost(PostReqDto reqDto, User user) {

        User curUser = userAuthCheck(user);

        Post post = postRepository.save(reqDto.toNoticePostEntity(curUser));
        PostNotifyResDto postReqDto = new PostNotifyResDto(post);

        return postReqDto;
    }

    public PostUpdateResDto updatePostByAdmin(long postId, PostReqDto reqDto, User user) {

       userAuthCheck(user);

        Post post = findById(postId);
        post.update(reqDto);

        return new PostUpdateResDto(post);
    }

    public void deletePostByAdmin(long postId, User user) {

        userAuthCheck(user);
        Post post = findById(postId);
        postRepository.delete(post);
    }

    @Transactional
    public PostUpdateResDto pinPost(long postId, boolean isPinned, User user) {
        userAuthCheck(user);
        Post post = findById(postId);
        post.setPin(isPinned);

        return new PostUpdateResDto(post);
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

    public User userAuthCheck(User user){
        User curUser = userService.findById(user.getId());
        if(curUser.getAuth().equals(Auth.USER)){
            throw new BusinessLogicException("Admin 권한이 필요합니다");
        }

        return curUser;
    }
}
