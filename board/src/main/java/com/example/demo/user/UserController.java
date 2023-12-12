package com.example.demo.user;

import com.example.demo.core.security.JwtTokenProvider;
import com.example.demo.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;


    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Error error) {
        userService.join(requestDTO);
        return ResponseEntity.ok( ApiUtils.success(null) );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDTO requestDTO, HttpServletRequest request, HttpServletResponse response) {
        String jwtToken = userService.login(requestDTO);

        // "Bearer " 접두사 제거
        jwtToken = jwtToken.replace(JwtTokenProvider.TOKEN_PREFIX, "");

        // 쿠키 설정
        Cookie cookie = new Cookie("token", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24*30); // 24시간 유효
        cookie.setPath("/"); // 모든 경로에서 쿠키 접근 가능
        response.addCookie(cookie);

        return ResponseEntity.ok().header(JwtTokenProvider.HEADER, jwtToken)
                .body(ApiUtils.success(jwtToken));
    }

    @PostMapping("/logout")
    public RedirectView logout(HttpServletResponse response) {
        // 쿠키에서 토큰을 삭제
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 쿠키 만료시간을 0으로 설정하여 삭제
        cookie.setPath("/");
        response.addCookie(cookie);

        // 로그아웃 성공 시 메인 페이지로 리다이렉트
        return new RedirectView("/");
    }

}
