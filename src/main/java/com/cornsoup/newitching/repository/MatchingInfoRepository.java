package com.cornsoup.newitching.repository;

import com.cornsoup.newitching.domain.MatchingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingInfoRepository extends JpaRepository<MatchingInfo, String> {
    boolean existsByUrl(String url);
}
