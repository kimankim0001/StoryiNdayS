package com.sparta.storyindays.enums.post;

import lombok.Getter;

@Getter
public enum PostType {
    NOTICE("공지"),
    NORMAL("일반");

    private final String state;

    PostType(String state){
        this.state = state;
    }
}
