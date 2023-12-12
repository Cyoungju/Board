package com.example.demo.home;


import com.example.demo.core.security.JwtTokenProvider;
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

    public String getJwt(Model model, HttpServletRequest request) {
        // 쿠키에서 토큰을 가져옵니다.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();

                    try {
                        // 토큰에서 이메일 정보를 가져옵니다.
                        String email = JwtTokenProvider.verify(token.replace(JwtTokenProvider.TOKEN_PREFIX, "")).getSubject();

                        // Optional을 안전하게 사용하세요.
                        Optional<User> userOptional = userService.findByUserEmail(email);

                        // 사용자가 존재하는 경우에만 model에 추가하세요.
                        if (userOptional.isPresent()) {
                            User user = userOptional.get();
                            model.addAttribute("user", user);
                        } else {
                             model.addAttribute("user", null);
                        }

                    } catch (Exception e) {
                        model.addAttribute("error", "토큰이 만료되었습니다. 다시 로그인해 주세요.");
                        return "redirect:/login"; // 로그인 페이지 URL에 맞게 수정
                    }

                    break;
                }
            }
        }
        return null;
    }

}


