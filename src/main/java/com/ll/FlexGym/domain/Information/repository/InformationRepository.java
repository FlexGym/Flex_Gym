package com.ll.FlexGym.domain.Information.repository;

import com.ll.FlexGym.domain.Information.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InformationRepository extends JpaRepository<Information, Long> {
    Optional findByVideoId(String videoId);

}
