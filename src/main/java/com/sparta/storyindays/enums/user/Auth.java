package com.sparta.storyindays.enums.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sparta.storyindays.exception.BusinessLogicException;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "enum : Auth")
public enum Auth {
    USER(UserType.USER),
    ADMIN(UserType.ADMIN);

    @JsonValue
    private final String user;

    Auth(String user) {
        this.user = user;
    }

    public String getUser() {
        return this.user;
    }

    public static class UserType {
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";
    }

    @JsonCreator
    public static Auth from(String authType){
        for(Auth auth : Auth.values()){
            if(auth.getUser().equals(authType)){
                return auth;
            }
        }
        log.debug("EnumCollection.Auth.from() exception occur authType: {}", authType);
        throw new BusinessLogicException("해당 type의 권한은 존재하지 않습니다.");
    }
}
