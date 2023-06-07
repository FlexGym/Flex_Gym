package com.ll.FlexGym.domain.youtube.repository;

import com.ll.FlexGym.domain.youtube.entity.YoutubeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YoutubeRepository extends JpaRepository<YoutubeEntity, Long>{
}

