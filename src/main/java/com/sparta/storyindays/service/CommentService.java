package com.sparta.storyindays.service;

import com.sparta.storyindays.dto.comment.CommentCreateReqDto;
import com.sparta.storyindays.dto.comment.CommentResDto;
import com.sparta.storyindays.entity.Comment;
import com.sparta.storyindays.entity.Post;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    //댓글작성
    @Transactional
    public CommentResDto createComment(long postId, CommentCreateReqDto reqDto, User user) {

        Post post = postService.findByPostId(postId);
        Comment comment = commentRepository.save(new Comment(reqDto.getComment(), post, user));
        return CommentResDto.toDto(commentRepository.save(comment));

    }
}
