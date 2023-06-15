package com.ll.FlexGym.domain.home;

import com.ll.FlexGym.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class HomeController {
    private final Rq rq;

    @GetMapping("/")
    public String showMain() {
        if (rq.isLogout()) return "redirect:/usr/main/home";

        return "redirect:/usr/main/home";
    }

    @GetMapping("/usr/main/home")
    public String showHome() {

        return "usr/main/home";
    }
}
