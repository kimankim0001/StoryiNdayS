package com.sparta.storyindays.enums.user;

public enum State {
    ACTIVATION(UserStateType.ACTIVATION),
    DEACTIVATION(UserStateType.DEACTIVATION),
    BLOCK(UserStateType.BLOCK);

    private final String state;

    State(String state){
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public static class UserStateType{
        public static final String ACTIVATION = "ACTIVATION";
        public static final String DEACTIVATION = "DEACTIVATION";
        public static final String BLOCK = "BLOCK";
    }
}
