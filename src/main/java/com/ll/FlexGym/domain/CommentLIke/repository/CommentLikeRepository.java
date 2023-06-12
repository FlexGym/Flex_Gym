package com.ll.FlexGym.domain.CommentLIke.repository;

import com.ll.FlexGym.domain.Comment.entity.Comment;
import com.ll.FlexGym.domain.CommentLIke.entity.CommentLike;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {



    boolean existsByCommentAndMember(Comment comment, Member member);


}
