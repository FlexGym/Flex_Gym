package com.ll.FlexGym.domain.Meeting.controller;

import com.ll.FlexGym.domain.ChatMember.entity.ChatMember;
import com.ll.FlexGym.domain.ChatMember.service.ChatMemberService;
import com.ll.FlexGym.domain.ChatRoom.service.ChatRoomService;
import com.ll.FlexGym.domain.Meeting.MeetingForm;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Meeting.service.MeetingService;
import com.ll.FlexGym.global.rq.Rq;
import com.ll.FlexGym.global.rsData.RsData;
import com.ll.FlexGym.global.security.SecurityMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ll.FlexGym.domain.ChatMember.entity.ChatMemberType.KICKED;

@Controller
@RequestMapping("/usr/meeting")
@RequiredArgsConstructor
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
    @GetMapping("/manage/{id}")
    public String manage(Model model, @PathVariable("id") Long id) {
        List<ChatMember> chatMemberList = chatMemberService.findByChatRoomId(id);
        model.addAttribute("chatMemberList", chatMemberList);
        model.addAttribute("KICKED", KICKED);
        return "usr/meeting/manage";
    }
}
