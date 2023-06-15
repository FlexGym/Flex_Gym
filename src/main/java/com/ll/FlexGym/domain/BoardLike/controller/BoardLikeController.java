package com.ll.FlexGym.domain.BoardLike.controller;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Board.service.BoardService;
import com.ll.FlexGym.domain.BoardLike.entity.BoardLike;
import com.ll.FlexGym.domain.BoardLike.service.BoardLikeService;
import com.ll.FlexGym.global.rq.Rq;
import com.ll.FlexGym.global.security.SecurityMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/usr")
@RequiredArgsConstructor
public class BoardLikeController {

    private final BoardLikeService boardLikeService;
    private final BoardService boardService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/{memberId}/boardLikeList")
    public String getBoardList(Model model, @PathVariable Long memberId, @AuthenticationPrincipal SecurityMember member){

        Long currentMemberId = member.getId();

        List<BoardLike> boardLikeList = boardLikeService.getListForMember(memberId, currentMemberId);
        List<Board> boardList = boardService.getListForMember(memberId, currentMemberId);

        if (boardLikeList == null) {
            return rq.historyBack("자신의 정보만 확인할 수 있습니다.");
        }

        model.addAttribute(boardLikeList);
        model.addAttribute(boardList);

        return "usr/board/myBoardLike_list";
    }

}
