package com.sparta.storyindays.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Test
    @DisplayName("게시글 작성")
    void writePost() {
    }


    @Nested
    @DisplayName("게시글 조회")
    class Test1 {
        @Test
        @DisplayName("게시글 조회 전체")
        void getAllPost() {
        }

        @Test
        void getUserPost() {
        }
    }

    @Test
    void updatePost() {
    }

    @Test
    void deletePost() {
    }

    @Test
    void findById() {
    }

    @Test
    void getPageable() {
    }

    @Test
    void validUserCheckAndGetUser() {
    }
}