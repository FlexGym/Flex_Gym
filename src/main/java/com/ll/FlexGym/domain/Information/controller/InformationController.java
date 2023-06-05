//package com.ll.FlexGym.domain.Information.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ll.FlexGym.domain.Information.service.InformationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//
//@RequiredArgsConstructor
//@Controller
//public class InformationController {
//
//    private final InformationService informationService;
//
//    @PreAuthorize("isAnonymous()")
//    @GetMapping("/usr/information/info")
//    public String showInfo() {
//        return "/usr/information/info";
//    }
//
//
//    @RequestMapping(value = {"/search"}, method = RequestMethod.GET)
//    public @ResponseBody
//    String searchYouTube(
//            @RequestParam(value = "word", required = true) String search,
//            @RequestParam(value = "items", required = false, defaultValue = "5") String items) throws JsonProcessingException {
//
//        int max = Integer.parseInt(items);
//        List<SearchItem> item = Search.youTubeSearch(search, max);
//
//        // JSON으로 변환
//        String result = new ObjectMapper().writeValueAsString(item);
//        System.out.println(result);
//        return result;
//    }
//
//}
