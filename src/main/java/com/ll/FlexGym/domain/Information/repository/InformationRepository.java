package com.ll.FlexGym.domain.Information.repository;

import com.ll.FlexGym.domain.Information.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationRepository extends JpaRepository<Information, Long> {

}
