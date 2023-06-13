package com.ll.FlexGym.domain.Comment.repository;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Comment.entity.Comment;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findByMember(Member member);
}
