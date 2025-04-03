package com.cornsoup.traitmatcher.repository;

import com.cornsoup.traitmatcher.domain.MatchingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingInfoRepository extends JpaRepository<MatchingInfo, String> {
    boolean existsByUrl(String url);
}
