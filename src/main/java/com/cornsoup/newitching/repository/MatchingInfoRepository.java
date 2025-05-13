package com.cornsoup.newitching.repository;

import com.cornsoup.newitching.domain.MatchingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchingInfoRepository extends JpaRepository<MatchingInfo, String> {
    boolean existsByUrl(String url);
    Optional<MatchingInfo> findByUrl(String url);
    Optional<MatchingInfo> findByMatchingId(String matchingId);
}
