package com.sparta.storyindays.repository;
import static com.sparta.storyindays.entity.QCommentLike.commentLike1;
import static com.sparta.storyindays.entity.QComment.comment1;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.storyindays.entity.Comment;
import com.sparta.storyindays.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> getLikesCommentWithPageAndSortDesc (User user, long offset, int pageSize) {
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.DESC, comment1.createdAt);

        return jpaQueryFactory.selectFrom(comment1)
                .join(commentLike1).on(commentLike1.comment.eq(comment1))
                .where(commentLike1.user.eq(user),commentLike1.commentLike.isTrue())
                .offset(offset)
                .limit(pageSize)
                .orderBy(orderSpecifier)
                .fetch();
    }
}
