package com.sparta.storyindays.repository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.storyindays.entity.Post;
import static com.sparta.storyindays.entity.QPostLike.postLike1;
import static com.sparta.storyindays.entity.QPost.post;
import com.sparta.storyindays.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getLikesPostWithPageAndSortDesc(User user, long offset, int pageSize) {
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.DESC, post.createdAt);

        return jpaQueryFactory.selectFrom(post)
                .join(postLike1).on(postLike1.post.eq(post))
                .where(postLike1.user.eq(user),postLike1.postLike.isTrue())
                .offset(offset)
                .limit(pageSize)
                .orderBy(orderSpecifier)
                .fetch();
    }
}
