package com.ll.FlexGym.domain.Comment.controller;


import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Board.service.BoardService;
import com.ll.FlexGym.domain.BoardLike.entity.BoardLike;
import com.ll.FlexGym.domain.Comment.entity.Comment;
import com.ll.FlexGym.domain.Comment.entity.CommentForm;
import com.ll.FlexGym.domain.Comment.service.CommentService;
import com.ll.FlexGym.domain.CommentLIke.entity.CommentLike;
import com.ll.FlexGym.domain.CommentLIke.repository.CommentLikeRepository;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.service.MemberService;
import com.ll.FlexGym.global.rq.Rq;
import com.ll.FlexGym.global.security.SecurityMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentLikeRepository commentLikeRepository;
    private final Rq rq;

    @PreAuthorize("isAuthenticated")
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Long id,
                                @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal){
        Board board = this.boardService.getBoard(id);
        Member member = this.memberService.getMember(principal.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("board", board);
            return "board_detail";
        }

        Comment parentComment = null;
        if (commentForm.getParentId() != null) {
            parentComment = commentService.getComment(commentForm.getParentId());
        }

        Comment comment = this.commentService.create(board, commentForm.getContent(), member, parentComment);
        return String.format("redirect:/usr/board/detail/%s#comment_%s",
                comment.getBoard().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String commentModify(CommentForm commentForm, @PathVariable("id") Long id, Principal principal) {
        Comment comment = this.commentService.getComment(id);

        if (!comment.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        commentForm.setContent(comment.getContent());
        return "usr/comment/comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                                @PathVariable("id") Long id, Principal principal)
    {
        if(bindingResult.hasErrors()){
            return "usr/comment/comment_form";
        }

        Comment comment = this.commentService.getComment(id);
        // 인증된 유저와 해당 댓글을 작성한 맴버의 아이디가 같을 때만 수행
        if(!comment.getMember().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다. ");
        }

        this.commentService.modify(comment,  commentForm.getContent());
        return String.format("redirect:/usr/board/detail/%s#comment_%s",
                comment.getBoard().getId(), comment.getId());

    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like/{id}")
    public String commentLike(Principal principal, @PathVariable("id") Long id){
        Comment comment = this.commentService.getComment(id);
        Member member = this.memberService.getMember(principal.getName());

        try{
            this.commentService.likeComment(comment,member);
        }catch (ResponseStatusException e){
            if (HttpStatus.FORBIDDEN.equals(e.getStatusCode())) {
                // Display an alert using JavaScript
                return String.format("redirect:/usr/board/detail/%s#comment_%s",
                        comment.getBoard().getId(), comment.getId());
            } else {
                // Handle other exceptions
                throw e;
            }
        }

        return String.format("redirect:/usr/board/detail/%s#comment_%s",
                comment.getBoard().getId(), comment.getId());

    }

    public void likeComment(Comment comment, Member member){
        // Check if the post has already been liked by the member
        boolean isLiked = commentLikeRepository.existsByCommentAndMember(comment, member);
        if(isLiked){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이미 좋아요 누른 댓글 입니다.");
        }

        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .member(member)
                .build();
        commentLikeRepository.save(commentLike);
        comment.addToCommentLikes(commentLike);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Long id){
        Comment comment = this.commentService.getComment(id);
        if(!comment.getMember().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/usr/board/detail/%s", comment.getBoard().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{memberId}/commentList")
    public String getCommentList(Model model, @PathVariable Long memberId, @AuthenticationPrincipal SecurityMember member){

        Long currentMemberId = member.getId();

        List<Comment> commentList = commentService.getListForMember(memberId, currentMemberId);

        if (commentList == null) {
            return rq.historyBack("자신의 정보만 확인할 수 있습니다.");
        }

        model.addAttribute(commentList);

        return "/usr/comment/myComment_list";
    }

}