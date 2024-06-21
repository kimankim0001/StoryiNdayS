package com.sparta.storyindays.service;

import com.sparta.storyindays.config.JwtConfig;
import com.sparta.storyindays.enums.user.Auth;
import com.sparta.storyindays.dto.user.LoginReqDto;
import com.sparta.storyindays.dto.user.SignupReqDto;
import com.sparta.storyindays.entity.PasswordHistory;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.exception.BusinessLogicException;
import com.sparta.storyindays.jwt.JwtProvider;
import com.sparta.storyindays.repository.PasswordHistoryRepository;
import com.sparta.storyindays.repository.UserRepository;
import com.sparta.storyindays.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AuthService")
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    @Value("${admin.secret.key}")
    private String ADMIN_TOKEN;

    public void signup(SignupReqDto signupReqDto) {

        String username = signupReqDto.getUsername();
        String password = passwordEncoder.encode(signupReqDto.getPassword());
        String name = signupReqDto.getName();
        Auth auth = signupReqDto.getAuthType();
        String email = signupReqDto.getEmail();

        //admin으로 회원가입시
        if (Auth.ADMIN.equals(auth)) {
            if(!ADMIN_TOKEN.equals(signupReqDto.getAuthToken())){
                throw new BusinessLogicException("관리자 암호가 틀려서 가입이 불가능합니다.");
            }
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "already.exist.username",
                    null,
                    "Already exist username",
                    Locale.getDefault()
            ));
        }

        User user = new User(username, password, name, auth, email);

        userRepository.save(user);

        //회원가입시 비밀번호가 히스토리에 저장
        PasswordHistory newHistory = new PasswordHistory();
        newHistory.setUser(user);
        newHistory.setPassword(user.getPassword());
        passwordHistoryRepository.save(newHistory);
    }

    @Transactional
    public List<String> login(LoginReqDto loginReqDto) {
        log.info("로그인 시도");
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginReqDto.getUsername(),
                        loginReqDto.getPassword(),
                        null
                )
        );

        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        String accessToken = jwtProvider.createToken(user, JwtConfig.accessTokenTime);
        String refreshToken = jwtProvider.createToken(user, JwtConfig.refreshTokenTime);

        List<String> tokens = new ArrayList<>();
        tokens.add(accessToken);
        tokens.add(refreshToken);

        user.updateRefreshToken(refreshToken);
        log.info("로그인 완료");
        return tokens;
    }


    public String reissue(String refreshToken) {
        log.info("토큰 재발행 시도");

        String subToken = jwtProvider.substringToken(refreshToken);
        Claims userInfo = jwtProvider.getUserInfoFromToken(subToken);
        User user = userRepository.findByUsername(userInfo.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new BusinessLogicException("위변조된 토큰입니다.");
        }

        if (jwtProvider.isExpiredToken(subToken)) {
            throw new BusinessLogicException("만료된 토큰입니다. 다시 로그인해주세요.");
        }

        return jwtProvider.createToken(user, JwtConfig.accessTokenTime);
    }
}
