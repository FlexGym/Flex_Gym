package com.ll.FlexGym.domain.Information.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

@Service
public class InformationService {

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
        String[] titleAndThumnailurl = new String[2];
        for(int i = 0; i < sl.size(); i++) {
            titleAndThumnailurl = sl.get(i).split("=");
        }
        titleAndThumnailurl[0] = titleAndThumnailurl[0].substring(1, titleAndThumnailurl[0].length());
        titleAndThumnailurl[1] = titleAndThumnailurl[1].substring(0, titleAndThumnailurl[1].length()-1);
        String[] s = hs.keySet().toArray(new String[0]);
        for (String ssss: s){
            System.out.println(ssss);
        }
        System.out.println(titleAndThumnailurl[0]);
        System.out.println(titleAndThumnailurl[1]);
    }

}
