package com.ll.FlexGym.domain.Board.repository;

import com.ll.FlexGym.domain.Board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    @Query("select "
            + "distinct b "
            + "from Board b "
            + "left outer join Member m1 on b.member=m1 "
            + "left outer join Comment c on c.board=b "
            + "left outer join Member m2 on c.member=m2 "
            + "where "
            + "   b.title like %:kw% "
            + "   or b.content like %:kw% "
            + "   or m1.username like %:kw% "
            + "   or c.content like %:kw% "
            + "   or m2.username like %:kw% ")
    Page<Board> findAllByKeyword(String kw, Pageable pageable);
}
