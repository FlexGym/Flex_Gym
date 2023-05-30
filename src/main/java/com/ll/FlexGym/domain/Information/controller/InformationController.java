package com.ll.FlexGym.domain.Information.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class InformationController {

    @PreAuthorize("isAnonymous()")
    @GetMapping("/usr/information/info")
    public String showInfo(){
        return "/usr/information/info";
    }

}
