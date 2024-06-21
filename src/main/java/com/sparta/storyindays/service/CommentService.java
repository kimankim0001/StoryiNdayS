package com.sparta.storyindays.service;

import com.sparta.storyindays.dto.comment.CommentCreateReqDto;
import com.sparta.storyindays.dto.comment.CommentResDto;
import com.sparta.storyindays.dto.comment.CommentUpdateReqDto;
import com.sparta.storyindays.entity.Comment;
import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.enums.user.Auth;
import com.sparta.storyindays.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public CommentResDto createComment(long postId, CommentCreateReqDto reqDto, User user) {

        Post post = postService.findById(postId);
        Comment comment = commentRepository.save(new Comment(reqDto.getComment(), post, user));
        return new CommentResDto(comment.getId(), comment.getUser().getUsername(), comment.getComment());
    }

    public List<CommentResDto> getAllComment(long postId) {

        postService.findById(postId);
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        List<CommentResDto> commentResDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentResDtos.add(new CommentResDto(comment.getId(), comment.getUser().getUsername(), comment.getComment()));
        }
        return commentResDtos;
    }

    @Transactional
    public CommentResDto updateComment(long postId, long commentId, CommentUpdateReqDto reqDto, User user) {

        postService.findById(postId);
        Comment comment = findComment(commentId);
        String loginUsername = user.getUsername();
        String commentUsername = comment.getUser().getUsername();

        if (!loginUsername.equals(commentUsername)) {
            throw new IllegalArgumentException("댓글 작성자와 현재 사용자가 불일치합니다.");
        }

        comment.updateComment(reqDto.getComment());

        return new CommentResDto(comment.getId(), comment.getUser().getUsername(), comment.getComment());
    }

    @Transactional
    public void deleteComment(long postId, long commentId, User user) {

        postService.findById(postId);
        Comment comment = findComment(commentId);
        String loginUsername = user.getUsername();
        String commentUsername = comment.getUser().getUsername();

        if (!loginUsername.equals(commentUsername)) {
            throw new IllegalArgumentException("댓글 작성자와 현재 사용자가 불일치합니다.");
        }
        commentRepository.delete(comment);
    }

    @Transactional
    public CommentResDto updateCommentAdmin(long postId, long commentId, CommentUpdateReqDto reqDto, User user) {
        postService.findById(postId);
        Comment comment = findComment(commentId);
        Auth loginAuth = user.getAuth();

        if (!loginAuth.equals(Auth.ADMIN)) {
            throw new IllegalArgumentException("관리자만 수정 가능합니다.");
        }

        comment.updateComment(reqDto.getComment());

        return new CommentResDto(comment.getId(), comment.getUser().getUsername(), comment.getComment());
    }

    @Transactional
    public void deleteCommentAdmin(long postId, long commentId, User user) {

        postService.findById(postId);
        Comment comment = findComment(commentId);
        Auth loginAuth = user.getAuth();

        if (!loginAuth.equals(Auth.ADMIN)) {
            throw new IllegalArgumentException("관리자만 삭제 가능합니다.");
        }
        commentRepository.delete(comment);
    }

    public Comment findComment(long commentId) {

        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 ID에 맞는 댓글이 없습니다."));
    }


}
