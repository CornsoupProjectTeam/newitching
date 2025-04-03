package com.cornsoup.traitmatcher.service;

import com.cornsoup.traitmatcher.domain.MatchingInfo;
import com.cornsoup.traitmatcher.dto.MatchingRegisterRequest;
import com.cornsoup.traitmatcher.repository.MatchingInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingInfoRepository matchingInfoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public String registerMatching(MatchingRegisterRequest request) {
        String rawPassword = request.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        String matchingId = request.getMatchingId();

        // 1. 중복 ID 검사
        if (matchingInfoRepository.existsById(matchingId)) {
            throw new IllegalArgumentException("이미 존재하는 매칭 ID입니다.");
        }

        // 2. 랜덤 UUID 기반 URL 키 생성 (중복 검사 포함)
        String urlKey;
        String url;
        do {
            urlKey = UUID.randomUUID().toString().substring(0, 8);
            url = "/matching/" + urlKey;
        } while (matchingInfoRepository.existsByUrl(url)); // 중복이면 다시 생성

        // 3. DB에 저장
        MatchingInfo info = MatchingInfo.builder()
                .matchingId(matchingId)
                .password(encodedPassword)
                .memberCount(request.getMemberCount())
                .teamSize(request.getTeamSize())
                .deadline(request.getDeadline())
                .url(url)
                .build();
        matchingInfoRepository.save(info);

        return url;
    }
}
