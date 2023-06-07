package com.ll.FlexGym.domain.BoardLike.repository;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.BoardLike.entity.BoardLike;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Integer> {
    boolean existsByBoardAndMember(Board board, Member member);
}
