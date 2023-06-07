package com.ll.FlexGym.domain.youtube.controller;

import com.ll.FlexGym.domain.youtube.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/usr/youtube/")
@RequiredArgsConstructor
public class YoutubeController {


    private final YoutubeService youtubeService;

    public HashMap<String, HashMap<String, String>> injectYoutubeData() throws IOException {
        //youtubeService.getYoutubeData();


        return youtubeService.getYoutubeData();
    }



}
