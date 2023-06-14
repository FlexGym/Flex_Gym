package com.ll.FlexGym.domain.Favorite.controller;

import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import com.ll.FlexGym.domain.Favorite.service.FavoriteService;
import com.ll.FlexGym.domain.Information.service.InformationService;
import com.ll.FlexGym.domain.Member.service.MemberService;
import com.ll.FlexGym.global.rq.Rq;
import com.ll.FlexGym.global.security.SecurityMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/usr/information")
@Slf4j
public class FavoriteController {
    Rq rq;
    private final FavoriteService favoriteService;
    private final MemberService memberService;
    private final InformationService informationService;


    @GetMapping("/favorites/{infoId}")
    public String addFavorite(@PathVariable Long infoId, @AuthenticationPrincipal SecurityMember member) {
        // memberId와 informationId를 이용하여 Member와 Information 객체를 가져온다.
        // 예를 들어, MemberService, InformationService를 통해 가져올 수 있다고 가정한다.
        // 좋아요를 눌렀을 때 isFavorite을 통해 해당 유저의favorite이 이미 있는지

        if(!favoriteService.isFavorite(member.getId(), infoId)){
            log.info("isFavorite = {}", favoriteService.isFavorite(member.getId(), infoId));
            favoriteService.addFavorite(member.getId(), infoId);
        }else{
            favoriteService.removeFavorite(member.getId(), infoId);
        }
        return "redirect:/usr/information/info";
    }

    public List<Favorite> getInformation(Long informationId){
        return favoriteService.getFavoriteMemberId(informationId);
    }



//    @PostMapping("/favorites/remove")
//    public String removeFavorite(@RequestParam Long favoriteId) {
//        // favoriteId를 이용하여 Favorite 객체를 가져온다.
//        // 예를 들어, FavoriteService를 통해 가져올 수 있다고 가정한다.
//        Favorite favorite = favoriteService.getFavorite(favoriteId);
//
//        favoriteService.removeFavorite(favorite);
//
//        return "redirect:/usr/information/info";
//    }
//    ///favorites/remove?informationId=${informationId}
}
