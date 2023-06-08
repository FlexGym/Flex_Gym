package com.ll.FlexGym.domain.Information.controller;

//import com.ll.FlexGym.domain.Information.service.InformationService;

import com.ll.FlexGym.domain.Information.service.InformationService;
import com.ll.FlexGym.domain.youtube.controller.YoutubeController;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class InformationController {

    private final InformationService informationService;
    private final YoutubeController youtubeController;


    @GetMapping("/usr/information/info")
    public String showInfo() {
        return "/usr/information/info";
    }


    @PreAuthorize("isAnonymous()")//나중에 관리자로 바꿔야함
    @GetMapping("/usr/information/getYoutube")
    @ResponseBody
    public String saveYoutube() throws IOException {
        informationService.setYoutubeData(youtubeController.injectYoutubeData());
        return "데이터가 입력됐습니다";
    }

    @ResponseBody // 값 받아오기
    @GetMapping("/usr/information/showYoutube")
    public List<HashMap> showYoutubeAPI() throws IOException {
        List<HashMap> hs = new ArrayList<>();
        hs.add(youtubeController.injectYoutubeData());

        return hs;
        //y.id, title, jpg
    }

}
