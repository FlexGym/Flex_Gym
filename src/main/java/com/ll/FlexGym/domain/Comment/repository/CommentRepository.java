package com.ll.FlexGym.domain.Comment.repository;

import com.ll.FlexGym.domain.Comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {


}
