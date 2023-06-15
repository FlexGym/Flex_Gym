package com.ll.FlexGym.domain.Favorite.repository;

import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import com.ll.FlexGym.domain.Information.entity.InfoStatus;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByMemberIdAndInformationId(Long memberId, Long informationId);

    List<Favorite> findByMemberId(Long memberId);

}
