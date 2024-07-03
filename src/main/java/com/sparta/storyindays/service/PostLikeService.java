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
        if (post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "no.liked.own.post",
                    null,
                    "You Can Not Click Like Button On Your Own Posts",
                    Locale.getDefault()
            ));
        }

        Optional<PostLike> postLike = postLikeRepository.findByPostIdAndUser(postId, user);
        if (postLike.isEmpty()) {
            PostLike newPostLike = new PostLike(post, user, true);
            postLikeRepository.save(newPostLike);
            postService.increasePostLikes(postId);
        } else {
            if (postLike.get().isPostLike()) {
                throw new IllegalArgumentException(messageSource.getMessage(
                        "already.liked",
                        null,
                        "Already Liked This Post",
                        Locale.getDefault()
                ));
            } else {
                postLike.get().updatePostLike(true);
                postService.increasePostLikes(postId);
            }
        }
    }

    @Transactional
    public void cancelPostLike(long postId, User user) {

        Optional<PostLike> postLike = postLikeRepository.findByPostIdAndUser(postId, user);
        if (postLike.isEmpty()) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "never.liked.post",
                    null,
                    "Never Liked Post",
                    Locale.getDefault()
            ));
        } else {
            if (postLike.get().isPostLike()) {
                postLike.get().updatePostLike(false);
                postService.decreasePostLikes(postId);
            } else {
                throw new IllegalArgumentException(messageSource.getMessage(
                        "already.cancel.liked",
                        null,
                        "Already Cancel Liked This Post",
                        Locale.getDefault()
                ));
            }

        }
    }
}
