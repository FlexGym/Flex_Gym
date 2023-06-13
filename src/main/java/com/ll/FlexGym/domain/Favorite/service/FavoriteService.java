package com.ll.FlexGym.domain.Favorite.service;

import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import com.ll.FlexGym.domain.Favorite.repository.FavoriteRepository;
import com.ll.FlexGym.domain.Information.entity.Information;
import com.ll.FlexGym.domain.Information.service.InformationService;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.repository.MemberRepository;
import com.ll.FlexGym.domain.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final InformationService informationService;
    private final MemberService memberService;

//    @Transactional
//    public void addFavorite(Member member, Information information){
//        Favorite favorite = Favorite.builder()
//                .member(member)
//                .information(information)
//                .build();
//        favoriteRepository.save(favorite);
//    }

    @Transactional
    public void removeFavorite(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }

    public Favorite getFavorite(Long id){
        Optional<Favorite> of = favoriteRepository.findById(id);

        Favorite favorite = null;

        if(of.isPresent()){
            favorite = of.get();
        }

        return favorite;
    }

    @Transactional
    public void addFavorite(Long memberId, Long infoId) {
        Information information = informationService.findById(infoId);
        Member member = memberService.findByIdElseThrow(memberId);

        Favorite favorite = Favorite.builder()
                .member(member)
                .information(information)
                .build();

        favoriteRepository.save(favorite);
    }
}
