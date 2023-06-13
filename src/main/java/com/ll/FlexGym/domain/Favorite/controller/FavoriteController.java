package com.ll.FlexGym.domain.Favorite.controller;

import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import com.ll.FlexGym.domain.Favorite.service.FavoriteService;
import com.ll.FlexGym.domain.Information.entity.Information;
import com.ll.FlexGym.domain.Information.service.InformationService;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final MemberService memberService;
    private final InformationService informationService;

    @PostMapping("/favorites/add")
    public ResponseEntity<String> addFavorite(@RequestParam String memberName, @RequestParam Long informationId) {
        // memberId와 informationId를 이용하여 Member와 Information 객체를 가져온다.
        // 예를 들어, MemberService, InformationService를 통해 가져올 수 있다고 가정한다.
        Member member = memberService.getMember(memberName);
        Information information = informationService.getInformation(informationId);

        favoriteService.addFavorite(member, information);

        return ResponseEntity.ok("Favorite added successfully");
    }

    @PostMapping("/favorites/remove")
    public ResponseEntity<String> removeFavorite(@RequestParam Long favoriteId) {
        // favoriteId를 이용하여 Favorite 객체를 가져온다.
        // 예를 들어, FavoriteService를 통해 가져올 수 있다고 가정한다.
        Favorite favorite = favoriteService.getFavorite(favoriteId);

        favoriteService.removeFavorite(favorite);

        return ResponseEntity.ok("Favorite removed successfully");
    }

}
