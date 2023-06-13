package com.ll.FlexGym.domain.Favorite.repository;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import com.ll.FlexGym.domain.Information.entity.Information;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
//    boolean existsByInformationAndMember(Information information, Member member);
}
