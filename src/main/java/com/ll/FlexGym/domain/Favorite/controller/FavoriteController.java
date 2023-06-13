package com.ll.FlexGym.domain.Favorite.controller;

import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import com.ll.FlexGym.domain.Favorite.service.FavoriteService;
import com.ll.FlexGym.domain.Information.entity.Information;
import com.ll.FlexGym.domain.Information.service.InformationService;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.service.MemberService;
import com.ll.FlexGym.global.security.SecurityMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/usr/information")
@Slf4j
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final MemberService memberService;
    private final InformationService informationService;


    @GetMapping("/favorites/{infoId}")
    public String addFavorite(@PathVariable Long infoId, @AuthenticationPrincipal SecurityMember member) {
        // memberId와 informationId를 이용하여 Member와 Information 객체를 가져온다.
        // 예를 들어, MemberService, InformationService를 통해 가져올 수 있다고 가정한다.
        favoriteService.addFavorite(member.getId(), infoId);
        log.info("member.Id = {}", member.getId());
        return "redirect:/usr/information/info";
    }



    @PostMapping("/favorites/remove")
    public String removeFavorite(@RequestParam Long favoriteId) {
        // favoriteId를 이용하여 Favorite 객체를 가져온다.
        // 예를 들어, FavoriteService를 통해 가져올 수 있다고 가정한다.
        Favorite favorite = favoriteService.getFavorite(favoriteId);

        favoriteService.removeFavorite(favorite);

        return "redirect:/usr/information/info";
    }
    ///favorites/remove?informationId=${informationId}
}
