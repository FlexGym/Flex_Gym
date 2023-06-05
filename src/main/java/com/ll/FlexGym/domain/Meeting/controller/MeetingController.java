package com.ll.FlexGym.domain.Meeting.controller;

import com.ll.FlexGym.domain.Meeting.MeetingForm;
import com.ll.FlexGym.domain.Meeting.entity.Meeting;
import com.ll.FlexGym.domain.Meeting.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/usr/meeting")
@RequiredArgsConstructor
public class MeetingController {

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
    public String meetingCreate(MeetingForm meetingForm) {
        return "usr/meeting/form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String meetingCreate(@Valid MeetingForm meetingForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "usr/meeting/form";
        }
        meetingService.create(meetingForm.getSubject(), meetingForm.getCapacity(), meetingForm.getLocation(), LocalDateTime.parse(meetingForm.getDateTime()), meetingForm.getContent());
        return "redirect:/usr/meeting/list";
    }
}
