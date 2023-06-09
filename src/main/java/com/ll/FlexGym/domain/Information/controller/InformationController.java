package com.ll.FlexGym.domain.Information.controller;

//import com.ll.FlexGym.domain.Information.service.InformationService;

import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import com.ll.FlexGym.domain.Favorite.service.FavoriteService;
import com.ll.FlexGym.domain.Information.entity.InfoStatus;
import com.ll.FlexGym.domain.Information.entity.Information;
import com.ll.FlexGym.domain.Information.service.InformationService;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import com.ll.FlexGym.domain.Member.service.MemberService;
import com.ll.FlexGym.domain.youtube.controller.YoutubeController;
import com.ll.FlexGym.global.security.SecurityMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@RequiredArgsConstructor
@Controller
@Slf4j
public class InformationController {

    private final InformationService informationService;
    private final YoutubeController youtubeController;
    private final MemberService memberService;
    private final FavoriteService favoriteService;

    @PreAuthorize("isAnonymous()")//나중에 관리자로 바꿔야함
    @GetMapping("/usr/information/getYoutube")
    @ResponseBody
    public String saveYoutube() throws IOException {
        informationService.setYoutubeData(youtubeController.injectYoutubeData());
        return "데이터가 입력됐습니다";
    }

    //관리자페이지
    @GetMapping("/usr/information/admin")
    public String showAdmin(Model model) {
        List<Information> informationList = this.informationService.getList(InfoStatus.WAIT);
        log.info("ssss = {}", informationList);
        model.addAttribute("informationList", informationList);

        return "usr/information/admin";
        //y.id, title, jpg
    }

    @GetMapping("/usr/information/info")
    public String showInfo(Model model, @AuthenticationPrincipal SecurityMember member) {
        List<Information> informationList = this.informationService.getList(InfoStatus.ON);
        if (member != null) {
            Member member1 = null;
            Optional<Member> byUsername = memberService.findByUsername(member.getUsername());
            if (byUsername.isPresent()) {
                member1 = byUsername.get();
            }
            model.addAttribute("member", member1);
        }

        model.addAttribute("informationList", informationList);

        return "usr/information/info";
        //y.id, title, jpg
    }

    public List<Information> showMainInfo(){
        List<Favorite> favoriteList = favoriteService.getFavorites();
        List<Information> informationList= new ArrayList<>();

        for (Favorite favorite : favoriteList) {
            informationList.add(favorite.getInformation());
        }

        for(Information info : informationList){
            if(info.getStatus() == InfoStatus.WAIT){
                informationList.remove(info);
            }
        }

        informationService.sort(informationList);

        List<Information> mainInfo = new ArrayList<>();

        if(!informationList.isEmpty()){
            int index = 0;
            mainInfo.add(informationList.get(index++));
            if(informationList.size()>1) {
                mainInfo.add(informationList.get(index));
            }
            return mainInfo;
        }

        return mainInfo;

    }

    @GetMapping("/usr/information/favorite/{memberId}")
    public String showFavorite(Model model, @AuthenticationPrincipal SecurityMember member, @PathVariable Long memberId) {
        List<Favorite> favoriteList = favoriteService.getFavoriteMemberId(memberId);

        List<Information> informationList= new ArrayList<>();



        for (Favorite favorite : favoriteList) {
            informationList.add(favorite.getInformation());
        }

        if (member != null) {
            Member member1 = null;
            Optional<Member> byUsername = memberService.findByUsername(member.getUsername());
            if (byUsername.isPresent()) {
                member1 = byUsername.get();
            }
            model.addAttribute("member", member1);
        }
//        Member checkMember = memberService.findByIdElseThrow(memberId);
        model.addAttribute("informationList", informationList);

        return "usr/information/info";
        //y.id, title, jpg
    }

    //api확인 페이지
    @ResponseBody
    @GetMapping("/usr/information/showYoutube")
    public List<HashMap> showYoutubeAPI() throws IOException {
        List<HashMap> hs = new ArrayList<>();
        hs.add(youtubeController.injectYoutubeData());

        return hs;
        //y.id, title, jpg
    }

    //값을 받아왔다면 해당 uri에 대한 정보를 jpa를 통해 데이터호출 후 폼에 자동입력
    //값이 없다면 일반입력
    @GetMapping("/usr/information/adminToInfo_form")
    public String getForm(@RequestParam(required = false) String videoId, Model model) {
        Optional<Information> video =
                this.informationService.getInformationByVideoId(videoId);
        if (video.isPresent()) {
            model.addAttribute("video", video.get());
        }
        return "usr/information/adminToInfo_form";
    }


    @PostMapping("/usr/information/adminToInfo_form")
    public String submitForm(@RequestParam Long id, @RequestParam String content) {
        //여기에 각자 위치 구현
        Information video = this.informationService.getInformation(id);

        informationService.create(id, content);
        //model.addAttribute("informationList", video);

        return "redirect:/usr/information/info";
    }

}
