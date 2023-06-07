package com.ll.FlexGym.domain.Information.service;

import com.ll.FlexGym.domain.Information.entity.Information;
import com.ll.FlexGym.domain.Information.repository.InformationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class InformationService {

    private final InformationRepository informationRepository;
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

//        private String content;
//
//        private String videoId;
//        private String title;
//        private String videoThumnailUrl;
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
