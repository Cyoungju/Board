package com.example.demo.home;


import com.example.demo.core.security.JwtTokenProvider;
import com.example.demo.user.TokenHolder;
import com.example.demo.user.User;
import com.example.demo.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HomeService {

    private final UserService userService;

    public void getJwt(Model model, HttpServletRequest request){

        // 쿠키에서 토큰을 가져옵니다.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    // 토큰에서 이메일 정보를 가져옵니다.
                    String email = JwtTokenProvider.verify(token.replace(JwtTokenProvider.TOKEN_PREFIX, "")).getSubject();
                    Optional<User> userOptional = userService.findByUserEmail(email);
                    User user = userOptional.get();

                    model.addAttribute("user", user);

                    break;
                }
            }
        }

    }
}
