package com.ll.FlexGym.domain.home;

import com.ll.FlexGym.domain.Favorite.service.FavoriteService;
import com.ll.FlexGym.domain.Information.controller.InformationController;
import com.ll.FlexGym.domain.Information.entity.Information;
import com.ll.FlexGym.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class HomeController {
    private final Rq rq;

    private final FavoriteService favoriteService;
    private final InformationController informationController;

    @GetMapping("/")
    public String showMain() {
        if (rq.isLogout()) return "redirect:/usr/main/home";

        return "redirect:/usr/main/home";
    }

    @GetMapping("/usr/main/home")
    public String showHome(Model model) {

        //favorite갯수에 따른 출력
        List<Information> il = informationController.showMainInfo();

        if(!il.isEmpty()) {
            model.addAttribute("Information", il);
        }

        return "usr/main/home";
    }
}
