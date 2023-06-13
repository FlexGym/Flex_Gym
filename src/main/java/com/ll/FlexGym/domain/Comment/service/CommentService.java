package com.ll.FlexGym.domain.Comment.service;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Comment.entity.Comment;
import com.ll.FlexGym.domain.Comment.repository.CommentRepository;
import com.ll.FlexGym.domain.CommentLIke.entity.CommentLike;
import com.ll.FlexGym.domain.CommentLIke.repository.CommentLikeRepository;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.repository.MemberRepository;
import com.ll.FlexGym.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;

    public Comment create(Board board, String content, Member member, Comment parentComment){
        Comment comment = Comment.builder()
                .content(content)
                .member(member)
                .board(board)
                .parent(parentComment)
                .build();
        this.commentRepository.save(comment);

        // Add the new comment as a child to the parent comment
        if (parentComment != null) {
            parentComment.addChildComment(comment);
            this.commentRepository.save(parentComment);
        }

        return comment;
    }

    public Comment getComment(Long id){
        Optional<Comment> comment = this.commentRepository.findById(id);
        if(comment.isPresent()){
            return comment.get();
        } else {
            throw new DataNotFoundException("comment not found");
        }
    }


    public void modify(Comment comment, String content){
        comment.updateContent(content);;
        this.commentRepository.save(comment);
    }

    public void delete(Comment comment){
        this.commentRepository.delete(comment);
    }

    public void likeComment(Comment comment, Member member){
        // 해당 댓글이 이 맴버에 의해 이전에 좋아요 체크된 적 있는 지 체크
        boolean isLiked = commentLikeRepository.existsByCommentAndMember(comment,member);
        if(isLiked){
            // 한 댓글당 좋아요 한개만 누를 수 있도록 제한
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이미 좋아요 누른 댓글입니다. ");
        }

        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .member(member)
                .build();

        commentLikeRepository.save(commentLike);

        comment.addToCommentLikes(commentLike);

    }


    public List<Comment> getListForMember(Long memberId, Long currentMemberId) {

        Optional<Member> member = memberRepository.findById(memberId);
        Member foundMember = findByMemberId(member, currentMemberId);

        if (foundMember == null) {
            return null;
        }

        return commentRepository.findByMember(foundMember);
    }


    private Member findByMemberId(Optional<Member> member, Long memberId) {
        if (member.isPresent()) {
            Member foundMember = member.get();
            if (foundMember.getId().equals(memberId)) {
                return foundMember;
            }
        }

        return null;
    }

}