package com.sparta.storyindays.util;

import com.sparta.storyindays.entity.Post;
import org.springframework.data.domain.*;

import java.util.List;

public class Utils {
    public static Pageable getPageable(int page, boolean isAsc) {
        // 정렬방향, 정렬 기준(생성일자 고정), 페이저블 생성
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "createdAt");
        Pageable pageable = PageRequest.of(page, 5, sort);

        return pageable;
    }

    public static <T> Page<T> getCustomPage(Pageable pageable, List<T> pagedList){
        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), pagedList.size());
        Page<T> pagingList = new PageImpl<>(pagedList.subList(start, end), pageable, pagedList.size());
        return pagingList;
    }
}
