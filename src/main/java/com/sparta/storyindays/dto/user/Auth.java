package com.sparta.storyindays.dto.user;

public enum Auth {
    USER(UserType.USER),
    ADMIN(UserType.ADMIN);

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
}
