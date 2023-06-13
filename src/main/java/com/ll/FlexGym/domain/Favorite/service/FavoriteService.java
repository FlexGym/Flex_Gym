package com.ll.FlexGym.domain.Favorite.service;

import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import com.ll.FlexGym.domain.Favorite.repository.FavoriteRepository;
import com.ll.FlexGym.domain.Information.entity.Information;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public void addFavorite(Member member, Information information){
        Favorite favorite = Favorite.builder()
                .member(member)
                .information(information)
                .build();
        favoriteRepository.save(favorite);
    }

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

}
