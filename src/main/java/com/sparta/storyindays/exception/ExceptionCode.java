package com.sparta.storyindays.exception;

public enum ExceptionCode {
    INVALID_AUTHTYPE(CodeType.INVALID_AUTHTYPE);

    private final String code;

    ExceptionCode(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    public static class CodeType{
        public static final String INVALID_AUTHTYPE = "해당 type의 권한은 존재하지 않습니다.";
    }
}
