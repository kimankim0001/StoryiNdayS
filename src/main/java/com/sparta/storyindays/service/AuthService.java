package com.sparta.storyindays.service;

import com.sparta.storyindays.dto.user.Auth;
import com.sparta.storyindays.dto.user.SignupReqDto;
import com.sparta.storyindays.dto.user.State;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    public void signup(SignupReqDto signupReqDto) {

        String username = signupReqDto.getUsername();
        String password = passwordEncoder.encode(signupReqDto.getPassword());
        String name = signupReqDto.getName();
        Auth auth = signupReqDto.getAuth();
        String email = signupReqDto.getEmail();

        if (userRepository.findByUsername(signupReqDto.getName()).isPresent()) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "already.exist.username",
                    null,
                    "Already exist username",
                    Locale.getDefault()
            ));
        }

        User user = new User(username, password, name, auth, email);

        userRepository.save(user);

    }
}
