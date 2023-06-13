package com.ll.FlexGym.domain.Favorite.repository;

import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {


}
