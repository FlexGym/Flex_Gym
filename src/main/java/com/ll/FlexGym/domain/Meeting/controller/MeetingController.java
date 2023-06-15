package com.ll.FlexGym.domain.Meeting.controller;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.ChatMember.entity.ChatMember;
import com.ll.FlexGym.domain.ChatMember.service.ChatMemberService;
import com.ll.FlexGym.domain.ChatRoom.entity.ChatRoom;
import com.ll.FlexGym.domain.ChatRoom.service.ChatRoomService;
import com.ll.FlexGym.domain.Meeting.MeetingForm;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Meeting.service.MeetingService;
import com.ll.FlexGym.global.rq.Rq;
import com.ll.FlexGym.global.rsData.RsData;
import com.ll.FlexGym.global.security.SecurityMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;

import static com.ll.FlexGym.domain.ChatMember.entity.ChatMemberType.KICKED;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequestMapping("/usr/meeting")
@RequiredArgsConstructor
@Slf4j
public class MeetingController {

    private final Rq rq;
    private final MeetingService meetingService;
    private final ChatRoomService chatRoomService;
    private final ChatMemberService chatMemberService;

    @GetMapping("/list")
    public String showList(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Meeting> paging = meetingService.getList(page);
        model.addAttribute("paging", paging);
        return "usr/meeting/list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Meeting meeting = meetingService.getMeeting(id).orElse(null);
        model.addAttribute("meeting", meeting);
        return "usr/meeting/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(MeetingForm meetingForm) {
        return "usr/meeting/form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid MeetingForm meetingForm, @AuthenticationPrincipal SecurityMember member) {

        Meeting meeting = meetingService.create(meetingForm.getSubject(), rq.getMember(), meetingForm.getCapacity(),
                meetingForm.getLocation(), meetingForm.getDate(), meetingForm.getTime(), meetingForm.getContent());

        chatRoomService.createAndConnect(meetingForm.getSubject(), meeting, member.getId());

        return rq.redirectWithMsg("/usr/meeting/list", "새로운 모임이 등록되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        Meeting meeting = meetingService.getMeeting(id).orElse(null);

        RsData canDeleteRsData = meetingService.canDelete(rq.getMember(), meeting);

        if (canDeleteRsData.isFail()) return rq.historyBack(canDeleteRsData);

        RsData deleteRsData = meetingService.delete(meeting);

        if (deleteRsData.isFail()) return rq.historyBack(deleteRsData);

        return rq.redirectWithMsg("/usr/meeting/list", deleteRsData);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String showModify(MeetingForm meetingForm, @PathVariable("id") Long id) {

        Meeting meeting = meetingService.getMeeting(id).orElse(null);

        RsData canModifyRsData = meetingService.canModify(rq.getMember(), meeting);

        if (canModifyRsData.isFail()) return rq.historyBack(canModifyRsData);

        meetingForm.setSubject(meeting.getSubject());
        meetingForm.setCapacity(meeting.getCapacity());
        meetingForm.setLocation(meeting.getLocation());
        meetingForm.setDate(meeting.getDate());
        meetingForm.setTime(meeting.getTime());
        meetingForm.setContent(meeting.getContent());

        return "usr/meeting/form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid MeetingForm meetingForm, @PathVariable("id") Long id) {

        Meeting meeting = meetingService.getMeeting(id).orElse(null);

        // 모집인원(capacity) >= 모임 참여자 수(participantsCount) 되도록
        RsData checkCapaRsData = meetingService.checkCapacity(meetingForm.getCapacity(), meeting.getParticipantsCount());
        if (checkCapaRsData.isFail()) return rq.historyBack(checkCapaRsData);

        RsData<Meeting> rsData = meetingService.modify(meeting, meetingForm.getSubject(), meetingForm.getCapacity(),
                meetingForm.getLocation(), meetingForm.getDate(), meetingForm.getTime(), meetingForm.getContent());

        chatRoomService.updateChatRoomName(meeting.getChatRoom(), meetingForm.getSubject());

        return rq.redirectWithMsg("/usr/meeting/detail/%s".formatted(id), rsData);
    }

    // 모임 참여자 관리
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/chatMembers/{roomId}")
    public String manage(Model model, @PathVariable Long roomId, @AuthenticationPrincipal SecurityMember member) {
        ChatRoom chatRoom = chatRoomService.findById(roomId);
        List<ChatMember> chatMemberList = chatMemberService.findByChatRoomId(roomId);

        // 참가하지 않은 멤버의 URL 차단
        List<ChatMember> chatMembers = chatMemberService.findByChatRoomOwnerIdAndChatMember(roomId, member.getId());
        if (chatMembers == null) {
            return rq.historyBack("해당 방에 참가하지 않았습니다.");
        }

        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("chatMemberList", chatMemberList);
        model.addAttribute("KICKED", KICKED);
        return "usr/meeting/chatMembers";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{memberId}/meetingList")
    public String getMeetingList(Model model, @PathVariable Long memberId,
                                 @AuthenticationPrincipal SecurityMember member){

        Long currentMemberId = member.getId();

        List<Meeting> meetingList = meetingService.getListForMember(memberId, currentMemberId);

        if (meetingList == null) {
            return rq.historyBack("자신의 정보만 확인할 수 있습니다.");
        }

        model.addAttribute(meetingList);

        return "usr/meeting/myMeeting_list";
    }


    @GetMapping("/search")
    public String searchMeeting(Model model, @RequestParam @Nullable String keyword,
                                @PageableDefault(sort = "createDate", direction = DESC, size = 12) Pageable pageable){

        log.info("미팅 검색 시작");
        Page<Meeting> meetings = meetingService.searchMeeting(keyword, pageable);

        model.addAttribute("paging", meetings);

        return "usr/meeting/list";
    }
}
