package com.sparta.storyindays.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.storyindays.dto.ExceptionResDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "LogoutExceptionHandler")
@Component
public class LogoutExceptionHandler {
    public void handleLogoutException(HttpServletRequest request, HttpServletResponse response, Exception e) {

        ExceptionResDto exceptionResDto;

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        exceptionResDto = new ExceptionResDto(HttpServletResponse.SC_FORBIDDEN,e.getMessage());

        try{
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResDto));

        }catch (IOException exception) {
            log.info("IOException");
        }
    }
}
