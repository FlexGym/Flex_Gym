package com.ll.FlexGym.domain.Board.service;


import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Board.repository.BoardRepository;
import com.ll.FlexGym.domain.BoardLike.entity.BoardLike;
import com.ll.FlexGym.domain.BoardLike.repository.BoardLikeRepository;
import com.ll.FlexGym.domain.ChatMember.entity.ChatMember;
import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Meeting.entity.SearchQuery;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.repository.MemberRepository;
import com.ll.FlexGym.global.exception.DataNotFoundException;
import com.ll.FlexGym.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final ApplicationEventPublisher publisher;
    private final Rq rq;


    public List<Board> getBoardList(){
        return this.boardRepository.findAll();
    }

    public Page<Board> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page,10,Sort.by(sorts));

        return this.boardRepository.findAllByKeyword(kw,pageable);
    }

    public void create(String title, String category, String content, Member member){
        Board b = Board
                .builder()
                .title(title).category(category).content(content)
                .member(member)
                .build();
        this.boardRepository.save(b);
    }

    public Board getBoard(Long id){
        Optional<Board> board = this.boardRepository.findById(id);
        if(board.isPresent()){
            return board.get();
        }
        else {
            throw new DataNotFoundException("board not found");
        }
    }

    @Transactional
    public void modify(Board board,String category, String title, String content) {
        board.updateCategory(category);
        board.updateTitle(title);
        board.updateContent(content);
        this.boardRepository.save(board);
    }


    public void delete(Board board) {
        this.boardRepository.delete(board);
    }


    public void likeBoard(Board board, Member member){
        // 해당 게시글이 이 맴버에 의해 이전에 좋아요 체크된 적 있는 지 체크
        boolean isLiked = boardLikeRepository.existsByBoardAndMember(board,member);
        if(isLiked){
            // 한 게시글 당 좋아요 한개망 누를 수 있도록 제한
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이미 좋아요 누른 게시글입니다.");

        }

        BoardLike boardLike = BoardLike.builder()
                .board(board)
                .member(member)
                .build();
        boardLikeRepository.save(boardLike);
        board.addToBoardLikes(boardLike);
    }


    public List<Board> getBoardListByCategory(String kw) {
        return boardRepository.findAllByCategory(kw);
    }

    public List<Board> getPopularBoardList() {
        return boardRepository.findTop10ByOrderByLikes();
    }


    public List<Board> getListForMember(Long memberId, Long currentMemberId) {

        Optional<Member> member = memberRepository.findById(memberId);
        Member foundMember = findByMemberId(member, currentMemberId);

        if (foundMember == null) {
            return null;
        }

        return boardRepository.findByMember(foundMember);
    }

    public List<Board> getListForMemberLimit(Long memberId, Long currentMemberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Member foundMember = findByMemberId(member, currentMemberId);

        if (foundMember == null) {
            return null;
        }

        List<Board> boardList = boardRepository.findByMember(foundMember);
        Collections.sort(boardList, Comparator.comparing(Board::getCreateDate).reversed());

        int limit = 4;
        int size = Math.min(boardList.size(), limit);
        return boardList.subList(0, size);
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

    public Page<Board> getList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> getListByCategory(Pageable pageable, String category) {
        return boardRepository.findByCategory(category, pageable);
    }


    public Page<Board> searchBoard(String keyword, Pageable pageable) {
        SearchQuery searchQuery = new SearchQuery(keyword);
        Page<Board> boards = boardRepository.findByTitleContainingIgnoreCase(searchQuery.getValue(), pageable);

        return boards;
    }
}