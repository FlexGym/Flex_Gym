package com.ll.FlexGym.domain.Meeting.controller;

import com.ll.FlexGym.domain.Meeting.MeetingForm;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Meeting.service.MeetingService;
import com.ll.FlexGym.global.rq.Rq;
import com.ll.FlexGym.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public String create(MeetingForm meetingForm) {
        return "usr/meeting/form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid MeetingForm meetingForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "usr/meeting/form";
        }
        meetingService.create(meetingForm.getSubject(), rq.getMember(), meetingForm.getCapacity(), meetingForm.getLocation(), meetingForm.getDateTime(), meetingForm.getContent());
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(MeetingForm meetingForm, @PathVariable("id") Integer id) {

        Meeting meeting = meetingService.getMeeting(id).orElse(null);

//        RsData canModifyRsData = meetingService.canModify(rq.getMember(), meeting);
//
//        if (canModifyRsData.isFail()) return rq.historyBack(canModifyRsData);

        meetingForm.setSubject(meeting.getSubject());
        meetingForm.setCapacity(meeting.getCapacity());
        meetingForm.setLocation(meeting.getLocation());
        meetingForm.setDateTime(meeting.getDateTime());
        meetingForm.setContent(meeting.getContent());

        return "usr/meeting/form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public  String Modify(@Valid MeetingForm meetingForm, BindingResult bindingResult, @PathVariable("id") Integer id) {
//        if (bindingResult.hasErrors()) {
//            return "question_form";
//        }
//
//        RsData<LikeablePerson> rsData = likeablePersonService.modifyAttractive(rq.getMember(), id, modifyForm.getAttractiveTypeCode());
//
//        if (rsData.isFail()) {
//            return rq.historyBack(rsData);
//        }

        Meeting meeting = meetingService.getMeeting(id).orElse(null);

        RsData<Meeting> rsData = meetingService.modify(meeting, meetingForm.getSubject(), meetingForm.getCapacity(), meetingForm.getLocation(), meetingForm.getDateTime(), meetingForm.getContent());

        return rq.redirectWithMsg("/usr/meeting/list", rsData);
    }
}
