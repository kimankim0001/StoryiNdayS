package com.sparta.storyindays.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.storyindays.config.JwtConfig;
import com.sparta.storyindays.dto.user.KakaoUserInfoDto;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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
    private final RestTemplate restTemplate;
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

    @Transactional
    public String kakaoLogin(String kakaoAccessToken) throws JsonProcessingException {
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken);
        log.info("kakaoUserInfo 발급 완료");
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        String accessToken = jwtProvider.createToken(kakaoUser,JwtConfig.accessTokenTime);
        String refreshToken = jwtProvider.createToken(kakaoUser, JwtConfig.refreshTokenTime);
        kakaoUser.updateRefreshToken(refreshToken);
        log.info("로그인 완료");

        return accessToken;
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        log.info("필요시에 회원가입");
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();

                kakaoUser = new User(kakaoUserInfo.getNickname(), encodedPassword, email, Auth.USER, kakaoId);
            }

            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }
}
