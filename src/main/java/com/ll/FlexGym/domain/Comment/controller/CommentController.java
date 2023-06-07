package com.ll.FlexGym.domain.Comment.controller;


import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Board.service.BoardService;
import com.ll.FlexGym.domain.Comment.entity.Comment;
import com.ll.FlexGym.domain.Comment.entity.CommentForm;
import com.ll.FlexGym.domain.Comment.service.CommentService;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated")
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Integer id,
                                @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal){
        Board board = this.boardService.getBoard(id);
        Member member = this.memberService.getMember(principal.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("board", board);
            return "board_detail";
        }

        Comment comment = this.commentService.create(board,commentForm.getContent(),member);
        return String.format("redirect:/usr/board/detail/%s#comment_%s",
        comment.getBoard().getId(), comment.getId());

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id, Principal principal){
        if(bindingResult.hasErrors()){
            return "comment_form";
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
    public String commentLike(Principal principal, @PathVariable("id") Integer id){
        Comment comment = this.commentService.getComment(id);
        Member member = this.memberService.getMember(principal.getName());
        this.commentService.likeComment(comment,member);
        return String.format("redirect:/usr/comment/detail/%s",id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Integer id){
        Comment comment = this.commentService.getComment(id);
        if(!comment.getMember().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/usr/board/detail/%s", comment.getBoard().getId());
    }

}
