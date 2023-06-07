package com.ll.FlexGym.domain.Meeting.controller;

import com.ll.FlexGym.domain.ChatRoom.service.ChatRoomService;
import com.ll.FlexGym.domain.Meeting.MeetingForm;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Meeting.service.MeetingService;
import com.ll.FlexGym.global.rq.Rq;
import com.ll.FlexGym.global.rsData.RsData;
import com.ll.FlexGym.global.security.SecurityMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usr/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final Rq rq;
    private final MeetingService meetingService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/list")
    public String showList(Model model) {
        List<Meeting> meetingList = this.meetingService.getList();
        model.addAttribute("meetingList", meetingList);
        return "usr/meeting/list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Meeting meeting = meetingService.getMeeting(id).orElse(null);
        model.addAttribute("meeting", meeting);
        return "usr/meeting/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String meetingCreate(MeetingForm meetingForm) {
        return "usr/meeting/form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String meetingCreate(@Valid MeetingForm meetingForm, BindingResult bindingResult,
                                @AuthenticationPrincipal SecurityMember member) {
        if (bindingResult.hasErrors()) {
            return "usr/meeting/form";
        }
        Meeting meeting = meetingService.create(meetingForm.getSubject(), rq.getMember(), meetingForm.getCapacity(), meetingForm.getLocation(), meetingForm.getDateTime(), meetingForm.getContent());

        chatRoomService.createAndConnect(meetingForm.getSubject(), meeting, member.getId());

        return "redirect:/usr/meeting/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String cancel(@PathVariable Integer id) {
        Meeting meeting = meetingService.getMeeting(id).orElse(null);

        RsData canDeleteRsData = meetingService.canDelete(rq.getMember(), meeting);

        if (canDeleteRsData.isFail()) return rq.historyBack(canDeleteRsData);

        RsData deleteRsData = meetingService.delete(meeting);

        if (deleteRsData.isFail()) return rq.historyBack(deleteRsData);

        return rq.redirectWithMsg("/usr/meeting/list", deleteRsData);
    }
}
