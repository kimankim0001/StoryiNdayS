package com.sparta.storyindays.service;

import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.entity.PostLike;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostService postService;

    @Transactional
    public void addPostLike(long postId, User user) {

        Post post = postService.findById(postId);

        Optional<PostLike> postLike = postLikeRepository.findByPostIdAndUser(postId, user);
        if (postLike.isEmpty()) {
            PostLike newPostLike = new PostLike(post, user, true);
            postLikeRepository.save(newPostLike);
        } else {
            postLike.get().updatePostLike(true);
        }
    }

    @Transactional
    public void cancelPostLike(long postId, User user) {

        postService.findById(postId);

        Optional<PostLike> postLike = postLikeRepository.findByPostIdAndUser(postId, user);
        if (!postLike.isEmpty()) {
            postLike.get().updatePostLike(false);
        } else {
            throw new IllegalArgumentException("해당 게시글 좋아요를 누른적이 없습니다.");
        }
    }
}
