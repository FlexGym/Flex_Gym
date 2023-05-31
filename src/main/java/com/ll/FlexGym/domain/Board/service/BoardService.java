package com.ll.FlexGym.domain.Board.service;


import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.Board.repository.BoardRepository;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.global.exception.DataNotFoundException;
import com.ll.FlexGym.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ApplicationEventPublisher publisher;

    public Page<Board> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("created"));
        Pageable pageable = PageRequest.of(page,10,Sort.by(sorts));

        return this.boardRepository.findAllByKeyword(kw,pageable);
    }

    public void create(String title, String content, Member member){
        Board b = Board
            .builder()
            .title(title)
            .content(content)
            .member(member)
            .build();
        this.boardRepository.save(b);
    }

    public Board getBoard(Integer id){
        Optional<Board> board = this.boardRepository.findById(id);
        if(board.isPresent()){
            return board.get();
        }
        else {
            throw new DataNotFoundException("baord not found");
        }
    }

    public void modify(Board board, String title, String content) {
        board.updateTitle(title);
        board.updateContent(content);
        this.boardRepository.save(board);
            }

}
