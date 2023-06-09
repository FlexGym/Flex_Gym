package com.ll.FlexGym.domain.Board.controller;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Board.form.BoardForm;
import com.ll.FlexGym.domain.Board.service.BoardService;
import com.ll.FlexGym.domain.BoardLike.entity.BoardLike;
import com.ll.FlexGym.domain.BoardLike.repository.BoardLikeRepository;
import com.ll.FlexGym.domain.Comment.entity.CommentForm;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.service.MemberService;
import com.ll.FlexGym.global.rq.Rq;
import com.ll.FlexGym.global.security.SecurityMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Nullable;
import java.security.Principal;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RequestMapping("/usr")
@RequiredArgsConstructor
@Controller
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final Rq rq;

    private final BoardLikeRepository boardLikeRepository;


    @GetMapping("/board/list")
    public String list(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Board> boardPage;

        if (kw.isEmpty()) {
            boardPage = boardService.getList(pageable);
        } else {
            boardPage = boardService.getListByCategory(pageable, kw);
        }

        List<Board> boardList = boardPage.getContent();

        Map<Integer, String> category = new LinkedHashMap<>();
        category.put(1, "운동일지");
        category.put(2, "상체운동");
        category.put(3, "하체운동");
        category.put(4, "바디프로필");
        category.put(5, "식단");

        model.addAttribute("boardPage", boardPage);
        model.addAttribute("kw", kw);
        model.addAttribute("boardList", boardList);
        model.addAttribute("category", category);

        return "usr/board/board_list";
    }


    @GetMapping("/board/popular")
    public String popular(Model model) {
        List<Board> popularBoardList = boardService.getPopularBoardList();

        Map<Integer, String> category = new LinkedHashMap<>();
        category.put(1, "운동일지");
        category.put(2, "상체운동");
        category.put(3, "하체운동");
        category.put(4, "바디프로필");
        category.put(5, "식단");


        model.addAttribute("category", category);


        model.addAttribute("boardList", popularBoardList);
        return "usr/board/popular_board_list";
    }


    @GetMapping("/board/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id, CommentForm commentForm){
        Board board = this.boardService.getBoard(id);
        model.addAttribute("board", board);
        return "usr/board/board_detail";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/create")
    public String boardCreate(BoardForm boardForm, Model model) {

        Map<Integer,String> category = new LinkedHashMap<>();
        category.put(1,"운동일지");
        category.put(2,"상체운동");
        category.put(3,"하체운동");
        category.put(4,"바디프로필");
        category.put(5,"식단");


        model.addAttribute("category", category);
        return "usr/board/board_form";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/board/create")
    public String boardCreate(@Valid BoardForm boardForm,
                              BindingResult bindingResult,
                              Model model, @AuthenticationPrincipal SecurityMember member) {
        if (bindingResult.hasErrors()) {
            return "usr/board/board_form";
        }

        Member mem = this.memberService.getMember(member.getUsername());
        String category = boardForm.getCategory(); // 선택한 카테고리 값 가져오기
        if (category == null || category.isEmpty()) {
            // 카테고리를 선택하지 않은 경우 에러 처리
            bindingResult.rejectValue("category", "error.category", "카테고리를 선택해주세요.");
            return "usr/board/board_form";
        }

        this.boardService.create(boardForm.getTitle(), category, boardForm.getContent(), mem);

        return "redirect:/usr/board/list";
    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/modify/{id}")
    public String boardModify(BoardForm boardForm,Model model,
                              Principal principal, @PathVariable("id") Long id){
        Board board = this.boardService.getBoard(id);
        if(!board.getMember().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }

        Map<Integer,String> category = new LinkedHashMap<>();
        category.put(1,"운동일지");
        category.put(2,"상체운동");
        category.put(3,"하체운동");
        category.put(4,"바디프로필");
        category.put(5,"식단");

        model.addAttribute("category",category);

        boardForm.setCategory(board.getCategory());
        boardForm.setContent(board.getContent());
        boardForm.setTitle(board.getTitle());

        model.addAttribute("category",category);
        return "usr/board/board_form";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/board/modify/{id}")
    public String boardModify(@Valid BoardForm boardForm, BindingResult bindingResult,
                              Principal principal, @PathVariable("id") Long id){
        Board board = this.boardService.getBoard(id); // board 모든
        log.info("modify Id = {}", id);
        if(!board.getMember().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }

        this.boardService.modify(board, boardForm.getCategory(), boardForm.getTitle(), boardForm.getContent());
        return "redirect:/usr/board/detail/%d".formatted(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/delete/{id}")
    public String boardDelete(Principal principal, @PathVariable("id") Long id){
        Board board = this.boardService.getBoard(id);
        if( !board.getMember().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
        }
        this.boardService.delete(board);
        return "redirect:/usr/board/list";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/like/{id}")
    public String boardLike(Principal principal, @PathVariable("id") Long id){
        Board board = this.boardService.getBoard(id);
        Member member = this.memberService.getMember(principal.getName());

        try {
            this.boardService.likeBoard(board, member);
        } catch (ResponseStatusException e) {
            // Catch the exception thrown when the user has already liked the post
            if (HttpStatus.FORBIDDEN.equals(e.getStatusCode())) {
                // Display an alert using JavaScript
                return "redirect:/usr/board/detail/" + id + "?alert=liked";
            } else {
                // Handle other exceptions
                throw e;
            }
        }

        return "redirect:/usr/board/detail/" + id;
    }

    public void likeBoard(Board board, Member member){
        // Check if the post has already been liked by the member
        boolean isLiked = boardLikeRepository.existsByBoardAndMember(board, member);
        if(isLiked){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이미 좋아요 누른 게시글입니다.");
        }

        BoardLike boardLike = BoardLike.builder()
                .board(board)
                .member(member)
                .build();
        boardLikeRepository.save(boardLike);
        board.addToBoardLikes(boardLike);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/{memberId}/boardList")
    public String getBoardList(Model model, @PathVariable Long memberId, @AuthenticationPrincipal SecurityMember member){

        Long currentMemberId = member.getId();

        List<Board> boardList = this.boardService.getListForMember(memberId, currentMemberId);

        if (boardList == null) {
            return rq.historyBack("자신의 정보만 확인할 수 있습니다.");
        }

        model.addAttribute(boardList);

        return "usr/board/myBoard_list";
    }

    @GetMapping("/board/search")
    public String searchMeeting(Model model, @RequestParam @Nullable String keyword,
                                @PageableDefault(sort = "createDate", direction = DESC, size = 10) Pageable pageable){

        log.info("게시판 검색 시작");
        Page<Board> boards = boardService.searchBoard(keyword, pageable);
        log.info("boards = {}", boards);

        model.addAttribute("boardList", boards.getContent());
        model.addAttribute("boardPage", boards);

        return "usr/board/board_list";
    }
}