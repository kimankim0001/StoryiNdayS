package com.sparta.storyindays.service;

import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.entity.PostLike;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostService postService;
    private final MessageSource messageSource;

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
            throw new IllegalArgumentException(messageSource.getMessage(
                    "never.liked.post",
                    null,
                    "Never Liked Post",
                    Locale.getDefault()
            ));
        }
    }
}
