package com.ll.FlexGym.domain.Board.repository;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
        Page<Board> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

        List<Board> findAllByCategory(@Param("category") String category);

    @Query("SELECT b FROM Board b ORDER BY SIZE(b.boardLikes) DESC")
    List<Board> findTop10ByOrderByLikes(Pageable pageable);

    default List<Board> findTop10ByOrderByLikes() {
        return findTop10ByOrderByLikes(PageRequest.of(0, 10));
    }

    List<Board> findByMember(Member member);

}
