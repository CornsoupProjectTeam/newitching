package com.cornsoup.newitching.service;

import com.cornsoup.newitching.domain.MatchingInfo;
import com.cornsoup.newitching.domain.Team;
import com.cornsoup.newitching.dto.MatchingRegisterRequest;
import com.cornsoup.newitching.dto.TeamResultDto;
import com.cornsoup.newitching.repository.MatchingInfoRepository;
import com.cornsoup.newitching.repository.MemberRepository;
import com.cornsoup.newitching.repository.TeamRepository;
import com.cornsoup.newitching.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingInfoRepository matchingInfoRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TeamMatchingClient teamMatchingClient;

    public String registerMatching(MatchingRegisterRequest request) {

        String matchingId = request.getMatchingId();

        // 1. 중복 ID 검사
        if (matchingInfoRepository.existsById(matchingId)) {
            throw new IllegalArgumentException("이미 존재하는 매칭 ID입니다.");
        }

        // 2. 랜덤 UUID 기반 URL 키 생성 (중복 검사 포함)
        String urlkey;
        do {
            urlkey = UUID.randomUUID().toString().substring(0, 8);
        } while (matchingInfoRepository.existsByUrl(urlkey)); // 중복이면 다시 생성

        // 3. 패스워드 암호화
        String rawPassword = request.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 4. DB에 저장
        MatchingInfo info = MatchingInfo.builder()
                .matchingId(matchingId)
                .password(encodedPassword)
                .memberCount(request.getMemberCount())
                .teamSize(request.getTeamSize())
                .deadline(request.getDeadline())
                .url(urlkey)
                .build();
        matchingInfoRepository.save(info);

        log.info("매칭 등록 완료 - matchingId: {}", matchingId);
        return urlkey;
    }

    @Async
    public void monitorMatching(String matchingId) {
        MatchingInfo matchingInfo = matchingInfoRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매칭 ID가 없습니다."));

        int expectedCount = matchingInfo.getMemberCount();
        long actualCount = memberRepository.countByMatching(matchingInfo);

        if (expectedCount != actualCount) {
            log.info("팀매칭 멤버 수 부족 - matchingId: {} (현재 {}명 / 기대 {}명)", matchingId, actualCount, expectedCount);
            return;
        }

        long incompleteBig5 = memberRepository.countIncompleteBig5Members(matchingInfo);

        if (incompleteBig5 > 0) {
            log.info("Big5 점수 입력 대기 중 - matchingId: {}, 입력 안 된 인원: {}", matchingId, incompleteBig5);
            return;
        }

        // 모든 조건 충족, 팀 매칭 요청
        String token = jwtTokenProvider.generateServerToServerToken(matchingId);
        teamMatchingClient.requestTeamMatching(matchingId, token);
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
