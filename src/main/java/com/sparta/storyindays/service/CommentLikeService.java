package com.sparta.storyindays.service;

import com.sparta.storyindays.entity.Comment;
import com.sparta.storyindays.entity.CommentLike;
import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentService commentService;
    private final PostService postService;
    private final MessageSource messageSource;

    @Transactional
    public void addCommentLike(long postId, long commentId, User user) {

        Post post = postService.findById(postId);
        Comment comment = commentService.findComment(commentId);

        if (comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "no.liked.own.comment",
                    null,
                    "You can't click like button on your own comments",
                    Locale.getDefault()
            ));
        }

        Optional<CommentLike> commentLike = commentLikeRepository.findByPostIdAndCommentIdAndUser(postId, commentId, user);
        if (commentLike.isEmpty()) {
            CommentLike newCommentLike = new CommentLike(comment, post, user, true);
            commentLikeRepository.save(newCommentLike);
            commentService.increaseCommentLikes(postId, commentId);
        } else {
            if (commentLike.get().isCommentLike()) {
                throw new IllegalArgumentException(messageSource.getMessage(
                        "already.liked",
                        null,
                        "Already Liked This Comment",
                        Locale.getDefault()
                ));
            } else {
                commentLike.get().updateCommentLike(true);
                commentService.increaseCommentLikes(postId, commentId);
            }
        }
    }

    @Transactional
    public void cancelCommentLike(long postId, long commentId, User user) {

        postService.findById(postId);
        commentService.findComment(commentId);

        Optional<CommentLike> commentLike = commentLikeRepository.findByPostIdAndCommentIdAndUser(postId, commentId, user);
        if (commentLike.isEmpty()) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "never.liked.comment",
                    null,
                    "Never Liked Comment",
                    Locale.getDefault()
            ));
        } else {
           if (commentLike.get().isCommentLike()) {
               commentLike.get().updateCommentLike(false);
               commentService.decreaseCommentLikes(postId, commentId);
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
