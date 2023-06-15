package com.ll.FlexGym.domain.Member.controller;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Board.service.BoardService;
import com.ll.FlexGym.domain.BoardLike.entity.BoardLike;
import com.ll.FlexGym.domain.BoardLike.service.BoardLikeService;
import com.ll.FlexGym.domain.Comment.entity.Comment;
import com.ll.FlexGym.domain.Comment.service.CommentService;
import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import com.ll.FlexGym.domain.Favorite.service.FavoriteService;
import com.ll.FlexGym.domain.Information.entity.Information;
import com.ll.FlexGym.domain.Information.service.InformationService;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Meeting.service.MeetingService;
import com.ll.FlexGym.domain.Member.dto.JoinFormDto;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.service.MemberService;
import com.ll.FlexGym.global.rq.Rq;
import com.ll.FlexGym.global.rsData.RsData;
import com.ll.FlexGym.global.security.SecurityMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/usr/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final MeetingService meetingService;
    private final BoardLikeService boardLikeService;
    private final FavoriteService favoriteService;
    private final Rq rq;


    @GetMapping("/login")
    public String showLogin(@AuthenticationPrincipal SecurityMember member) {
        if (member != null){
            return rq.redirectWithMsg("/usr/main/home", "현재 로그인 상태입니다.");
        }
        return "usr/member/login";
    }
    @GetMapping("/home")
    public String showHome() {
        return "usr/main/home"; // Return the home page template
    }


    @PreAuthorize("isAuthenticated()")
    public String showMe(Model model) {
        return "usr/member/me";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "usr/member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinFormDto joinFormDto) {
        RsData<Member> joinRs = memberService.join(joinFormDto.getUsername(), joinFormDto.getPassword());

        if (joinRs.isFail()) {
            return rq.historyBack(joinRs);
        }

        return rq.redirectWithMsg("/usr/member/login", joinRs);
    }

    @PreAuthorize("isAuthenticated()") // 로그인 해야만 접속가능
    @GetMapping("/me") // 로그인 한 나의 정보 보여주는 페이지
    public String showMe(Model model, @AuthenticationPrincipal SecurityMember member) {
        Long memberId = member.getId();

        List<Board> myBoards = boardService.getListForMemberLimit(memberId, member.getId());
        List<Comment> myComments = commentService.getListForMemberLimit(memberId, member.getId());
        List<Meeting> myMeetings = meetingService.getListForMemberLimit(memberId, member.getId());
        List<BoardLike> myBoardLikes = boardLikeService.getListForMemberLimit(memberId, member.getId());
        List<Favorite> myFavoriteInfo = favoriteService.getFavoriteMemberId(member.getId());

        model.addAttribute("myBoards", myBoards);
        model.addAttribute("myComments", myComments);
        model.addAttribute("myMeetings", myMeetings);
        model.addAttribute("myBoardLikes", myBoardLikes);
        model.addAttribute("myFavoriteInfo", myFavoriteInfo);

        return "usr/member/me";
    }


}