package com.sparta.storyindays.service;

import com.sparta.storyindays.entity.Comment;
import com.sparta.storyindays.entity.CommentLike;
import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentService commentService;
    private final PostService postService;

    @Transactional
    public void addCommentLike(long postId, long commentId, User user) {

        Post post = postService.findById(postId);
        Comment comment = commentService.findComment(commentId);

        Optional<CommentLike> commentLike = commentLikeRepository.findByPostIdAndCommentIdAndUser(postId, commentId, user);
        if (commentLike.isEmpty()) {
            CommentLike newCommentLike = new CommentLike(comment, post, user, true);
            commentLikeRepository.save(newCommentLike);
        } else {
            commentLike.get().updateCommentLike(true);
        }
    }

    @Transactional
    public void cancelCommentLike(long postId, long commentId, User user) {

        postService.findById(postId);
        commentService.findComment(commentId);

        Optional<CommentLike> commentLike = commentLikeRepository.findByPostIdAndCommentIdAndUser(postId, commentId, user);
        if (!commentLike.isEmpty()) {
            commentLike.get().updateCommentLike(false);
        } else {
            throw new IllegalArgumentException("해당 댓글 좋아요를 누른적이 없습니다.");
        }

    }

}
