package com.sparta.storyindays.repository;

import static com.sparta.storyindays.entity.QPostLike.postLike1;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public long countPostLikesByUserId(Long userId) {
        Long count = jpaQueryFactory
                .select(Wildcard.count)
                .from(postLike1)
                .where(postLike1.user.id.eq(userId))
                .fetchOne();

        return Optional.ofNullable(count).orElse(0L);
    }
}
