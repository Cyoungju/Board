package com.example.demo.home;

import com.example.demo.core.security.JwtTokenProvider;
import com.example.demo.user.TokenHolder;
import com.example.demo.user.User;
import com.example.demo.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@RequiredArgsConstructor
@Controller
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        homeService.getJwt(model, request);
        return "index"; // 뷰 이름을 적절하게 변경해주세요.
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // "login.html" 파일을 렌더링
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

}
