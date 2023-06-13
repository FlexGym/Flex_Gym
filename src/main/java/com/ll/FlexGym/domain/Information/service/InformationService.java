package com.ll.FlexGym.domain.Information.service;

import com.ll.FlexGym.domain.Board.entity.Board;
import com.ll.FlexGym.domain.BoardLike.entity.BoardLike;
import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import com.ll.FlexGym.domain.Favorite.repository.FavoriteRepository;
import com.ll.FlexGym.domain.Information.entity.InfoStatus;
import com.ll.FlexGym.domain.Information.entity.Information;
import com.ll.FlexGym.domain.Information.repository.InformationRepository;
import com.ll.FlexGym.domain.Member.entitiy.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class InformationService {

    private final InformationRepository informationRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public Information create(String videoId, String title, String videoThumnailUrl){
        Information information = Information
                .builder()
                .videoId(videoId)
                .title(title)
                .videoThumnailUrl(videoThumnailUrl)
                .status(InfoStatus.WAIT)
                .build();
        informationRepository.save(information);

        return information;
    }

    @Transactional
    public Information create(Long id, String content){
        Optional<Information> oi = informationRepository.findById(id);
        Information infoData = null;
        if(oi.isPresent()){
             infoData = oi.get();
        }


        //videoId title 재설정할 필요가 있나?
        Information information = Information
                .builder()
                .id(infoData.getId())
                .videoId(infoData.getVideoId())
                .title(infoData.getTitle())
                .videoThumnailUrl(infoData.getVideoThumnailUrl())
                .content(content)
                .status(InfoStatus.ON)
                .build();
        informationRepository.save(information);

        return information;
    }

    @Transactional
    public List<Information> getList(InfoStatus status){
        //Optional informationList = this.informationRepository.findByStatus(status);
        System.out.println(status);
//        List<Information> oi = this.informationRepository.findByStatus(status);
        List<Information> informationList = this.informationRepository.findByStatus(status);


        return informationList;
    }

    public Optional getInformationByVideoId(String videoId){
        Optional<Information> oi = informationRepository.findByVideoId(videoId);

        return oi;
    }

    public Information getInformation(Long id){
        Optional<Information> oi = informationRepository.findById(id);
        Information information = null;
        if(oi.isPresent()){
            information = oi.get();
        }
        return information;
    }

    public void favoriteInfo(Information information, Member member){
        // 해당 게시글이 이 맴버에 의해 이전에 좋아요 체크된 적 있는 지 체크
        boolean isFavorited = favoriteRepository.existsByInformationAndMember(information,member);
        if(isFavorited){
            // 한 게시글 당 좋아요 한개만 누를 수 있도록 제한
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이미 즐겨찾기 되었습니다.");

        }

        Favorite favorite = Favorite.builder()
                .information(information)
                .member(member)
                .build();
        favoriteRepository.save(favorite);
        information.addToFavorite(favorite);
    }

    @Transactional
    public void setYoutubeData(HashMap youtubeData){
//        String videoId;
//        String title;
//        String videoThumnailUrl;
        HashMap<String, HashMap> hs = youtubeData;
        hs.get("videoId");
        System.out.println("key : " + hs.keySet());
        System.out.println("value : " + hs.values());
        Collection<HashMap> a = hs.values();
        Iterator<HashMap> aiter = a.iterator();
        ArrayList<String> sl = new ArrayList<>();
        while(aiter.hasNext()){
            sl.add(String.valueOf(aiter.next()));
        }

        for (String oo: sl) {
            System.out.println(oo);
        }
        String[] titleAndThumnailurl = new String[10];
        ArrayList<String> titleAndThumnailurl2 = new ArrayList<>();
        ArrayList<String> titleArray = new ArrayList<>();
        ArrayList<String> videoThumnailUrlArray = new ArrayList<>();
        for(int i = 0; i < sl.size(); i++) {
            titleAndThumnailurl = sl.get(i).split("=");
            titleAndThumnailurl[0] = titleAndThumnailurl[0].substring(1, titleAndThumnailurl[0].length());
            titleAndThumnailurl[1] = titleAndThumnailurl[1].substring(0, titleAndThumnailurl[1].length()-1);
            titleArray.add(titleAndThumnailurl[0]);
            videoThumnailUrlArray.add(titleAndThumnailurl[1]);
        }

        String[] s = hs.keySet().toArray(new String[0]);
        for (String ssss: s){
//            System.out.println(ssss);
            log.info("ssss = {}", ssss);
        }
        for(String sss : titleAndThumnailurl2){
//            System.out.println(sss);
            log.info("sss = {}", sss);
        }

        for(int i = 0; i < 5; i++) {
            Information information = Information
                    .builder()
                    .videoId(s[i])
                    .title(titleArray.get(i))
                    .videoThumnailUrl(videoThumnailUrlArray.get(i))
                    .build();
            informationRepository.save(information);
        }

    }

}
