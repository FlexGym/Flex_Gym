package com.ll.FlexGym.domain.Board.controller;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Board.entity.Category;
import com.ll.FlexGym.domain.Board.form.BoardForm;
import com.ll.FlexGym.domain.Board.service.BoardService;
import com.ll.FlexGym.domain.Comment.entity.CommentForm;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.service.MemberService;
import com.ll.FlexGym.global.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequestMapping("/usr")
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    private final Rq rq;

    @GetMapping("/board/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0")int page, @RequestParam(value = "kw", defaultValue = "")String kw){
        List<Board> boardList = this.boardService.getBoardList();
        Page<Board> paging = this.boardService.getList(page,kw);
        model.addAttribute("paging",paging);
        model.addAttribute("kw",kw);
        model.addAttribute("boardList",boardList);

                return "/usr/board/board_list";
    }

    @GetMapping("/board/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, CommentForm commentForm){
        Board board = this.boardService.getBoard(id);
        model.addAttribute("board", board);
        return "/usr/board/board_detail";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/create")
    public String boardCreate(BoardForm boardForm) {
        return "/usr/board/board_form";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/board/create")
    public String boardCreate(@Valid BoardForm boardForm,
                              BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "/usr/board/board_form";
        }

        Member member = this.memberService.getMember(rq.getMember().getUsername());
        this.boardService.create(boardForm.getTitle(),boardForm.getContent(),member);
        return "redirect:/usr/board/list";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/board/modify/{id}")
    public String boardModify(@Valid BoardForm boardForm, BindingResult bindingResult,
                              Rq rq, @PathVariable("id") Integer id){
        Board board = this.boardService.getBoard(id);
        if(!board.getMember().getUsername().equals(rq.getMember().getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }
        this.boardService.modify(board, boardForm.getTitle(),boardForm.getContent());
        return String.format("redirect:/usr/board/detail/%s",id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/delete/{id}")
    public String boardDelete(Principal principal, @PathVariable("id") Integer id){
        Board board = this.boardService.getBoard(id);
        if( !board.getMember().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
        }
        this.boardService.delete(board);
        return "redirect:/";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/like/{id}")
    public String boardLike(Principal principal, @PathVariable("id") Integer id){
        Board board = this.boardService.getBoard(id);
        Member member = this.memberService.getMember(principal.getName());
        this.boardService.likeBoard(board,member);
        return String.format("redirect:/usr/board/detail/%s",id);

    }


}
