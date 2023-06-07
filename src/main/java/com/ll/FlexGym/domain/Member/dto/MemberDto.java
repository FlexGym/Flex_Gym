package com.ll.FlexGym.domain.Member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    @JsonProperty("user_id")
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("created_at")
    private LocalDateTime createDate;

    @JsonProperty("updated_at")
    private LocalDateTime modifyDate;

    public static MemberDto fromUser(Member member) {
        MemberDto memberDto = MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .createDate(member.getCreateDate())
                .modifyDate(member.getModifyDate())
                .build();

        return memberDto;
    }
}
