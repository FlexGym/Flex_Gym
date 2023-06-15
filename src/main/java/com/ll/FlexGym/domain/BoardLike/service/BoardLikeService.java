package com.ll.FlexGym.domain.BoardLike.service;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.BoardLike.entity.BoardLike;
import com.ll.FlexGym.domain.BoardLike.repository.BoardLikeRepository;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardLikeService {

    private final MemberRepository memberRepository;
    private final BoardLikeRepository boardLikeRepository;

    public List<BoardLike> getListForMember(Long memberId, Long currentMemberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Member foundMember = findByMemberId(member, currentMemberId);

        if (foundMember == null) {
            return null;
        }

        return boardLikeRepository.findByMember(foundMember);
    }

    /**
     * 마이페이지에 보여주기 위하여 Limit 제한으로 목록 가져오기
     */
    public List<BoardLike> getListForMemberLimit(Long memberId, Long currentMemberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Member foundMember = findByMemberId(member, currentMemberId);

        if (foundMember == null) {
            return null;
        }

        List<BoardLike> boardLikeList = boardLikeRepository.findByMember(foundMember);
        Collections.sort(boardLikeList, Comparator.comparing(BoardLike::getCreateDate).reversed());

        int limit = 4;
        int size = Math.min(boardLikeList.size(), limit);
        return boardLikeList.subList(0, size);
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
