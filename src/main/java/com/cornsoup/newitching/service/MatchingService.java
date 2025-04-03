package com.cornsoup.newitching.service;

import com.cornsoup.newitching.domain.MatchingInfo;
import com.cornsoup.newitching.domain.Team;
import com.cornsoup.newitching.dto.MatchingRegisterRequest;
import com.cornsoup.newitching.dto.TeamResultDto;
import com.cornsoup.newitching.repository.MatchingInfoRepository;
import com.cornsoup.newitching.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingInfoRepository matchingInfoRepository;
    private final TeamRepository teamRepository;
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

    public List<TeamResultDto> getMatchingResults(String matchingId) {
        List<Team> teams = teamRepository.findTeamsByMatchingId(matchingId);

        return teams.stream().map(team ->
                TeamResultDto.builder()
                        .teamId(team.getTeamId())
                        .conscientiousnessSimilarityScore(team.getConscientiousnessSimilarityScore())
                        .conscientiousnessSimilarityEval(team.getConscientiousnessSimilarityEval())
                        .conscientiousnessMeanScore(team.getConscientiousnessMeanScore())
                        .conscientiousnessMeanEval(team.getConscientiousnessMeanEval())
                        .agreeablenessSimilarityScore(team.getAgreeablenessSimilarityScore())
                        .agreeablenessSimilarityEval(team.getAgreeablenessSimilarityEval())
                        .agreeablenessMeanScore(team.getAgreeablenessMeanScore())
                        .agreeablenessMeanEval(team.getAgreeablenessMeanEval())
                        .opennessDiversityEval(team.getOpennessDiversityEval())
                        .extraversionDiversityEval(team.getExtraversionDiversityEval())
                        .neuroticismSimilarityScore(team.getNeuroticismSimilarityScore())
                        .neuroticismSimilarityEval(team.getNeuroticismSimilarityEval())
                        .memberNames(team.getMembers().stream()
                                .map(member -> member.getDepartment() + " " + member.getName())
                                .collect(Collectors.toList()))
                        .build()
        ).toList();
    }
}
