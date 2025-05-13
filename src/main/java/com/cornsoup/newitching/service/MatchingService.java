package com.cornsoup.newitching.service;

import com.cornsoup.newitching.domain.MatchingInfo;
import com.cornsoup.newitching.domain.Team;
import com.cornsoup.newitching.dto.MatchingRegisterRequest;
import com.cornsoup.newitching.dto.MemberDto;
import com.cornsoup.newitching.dto.TeamResultDto;
import com.cornsoup.newitching.repository.MatchingInfoRepository;
import com.cornsoup.newitching.repository.MemberRepository;
import com.cornsoup.newitching.repository.TeamRepository;
import com.cornsoup.newitching.security.JwtTokenProvider;
import com.cornsoup.newitching.security.PasswordDecryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingInfoRepository matchingInfoRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PasswordDecryptor passwordDecryptor;
    private final JwtTokenProvider jwtTokenProvider;
    private final TeamMatchingClient teamMatchingClient;

    public void doubleCheckMatchingId(String matchingId) {
        if (matchingInfoRepository.existsById(matchingId)) {
            throw new IllegalArgumentException("이미 존재하는 매칭 ID입니다.");
        }
    }

    public String getMatchingIdByUrlKey(String urlKey) {
        return matchingInfoRepository.findByUrl(urlKey)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 URL 키입니다."))
                .getMatchingId();
    }

    public int getTeamSize(String matchingId) {
        MatchingInfo matchingInfo = matchingInfoRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매칭 ID가 없습니다."));
        return matchingInfo.getTeamSize();
    }

    public int getMemberCount(String matchingId) {
        MatchingInfo matchingInfo = matchingInfoRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매칭 ID가 없습니다."));
        return matchingInfo.getMemberCount();
    }

    public String registerMatching(MatchingRegisterRequest request) {

        String matchingId = request.getMatchingId();

        // 1. 중복 ID 검사
        if (matchingInfoRepository.existsById(matchingId)) {
            throw new IllegalArgumentException("이미 존재하는 매칭 ID입니다.");
        }

        // 2. memberCount와 teamSize 나누어떨어지는지 검증
        Integer memberCount = request.getMemberCount();
        Integer teamSize = request.getTeamSize();

        if (memberCount == null || teamSize == null || memberCount <= 0 || teamSize <= 0) {
            throw new IllegalArgumentException("멤버 수와 팀 크기는 0보다 커야 합니다.");
        }

        if (memberCount % teamSize != 0) {
            throw new IllegalArgumentException(
                    String.format("멤버 수(%d)와 팀 크기(%d)가 나누어떨어지지 않습니다.", memberCount, teamSize)
            );
        }

        // 3. 랜덤 UUID 기반 URL 키 생성 (중복 검사 포함)
        String urlkey;
        do {
            urlkey = UUID.randomUUID().toString().substring(0, 8);
        } while (matchingInfoRepository.existsByUrl(urlkey)); // 중복이면 다시 생성

        // 4. 패스워드 암호화
        String rawPassword = request.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 5. DB에 저장
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
        long actualCount = memberRepository.countByMatching_MatchingId(matchingId);

        if (expectedCount != actualCount) {
            log.info("팀매칭 멤버 수 부족 - matchingId: {} (현재 {}명 / 기대 {}명)", matchingId, actualCount, expectedCount);
            return;
        }

        long incompleteBig5 = memberRepository.countIncompleteBig5MembersById(matchingId);

        if (incompleteBig5 > 0) {
            log.info("Big5 점수 입력 대기 중 - matchingId: {}, 입력 안 된 인원: {}", matchingId, incompleteBig5);
            return;
        }

        // 모든 조건 충족, 팀 매칭 요청
        String token = jwtTokenProvider.generateServerToServerToken(matchingId);
        int teamSize = matchingInfo.getTeamSize();

        teamMatchingClient.requestTeamMatching(matchingId, token, teamSize);
    }

    public void validateMatchingIdAndPassword(String matchingId, String encryptedPassword) {
        MatchingInfo matchingInfo = matchingInfoRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매칭 ID입니다."));

        String rawPassword = passwordDecryptor.decrypt(encryptedPassword);

        if (!passwordEncoder.matches(rawPassword, matchingInfo.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public List<TeamResultDto> getMatchingResults(String matchingId) {
        // 1. 마감 기한 체크
//        MatchingInfo matchingInfo = matchingInfoRepository.findByMatchingId(matchingId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 매칭정보를 찾을 수 없습니다."));
//
//        if (matchingInfo.getDeadline().isAfter(LocalDateTime.now())) {
//            throw new IllegalStateException("매칭 결과는 마감기한 이후에 확인할 수 있습니다.");
//        }

        // 2. 팀 정보 조회 및 검증
        List<Team> teams = teamRepository.findTeamsByMatchingId(matchingId);

        boolean hasIncompleteTeam = teams.stream()
                .anyMatch(team -> team.getMembers() == null || team.getMembers().isEmpty());

        if (hasIncompleteTeam) {
            throw new IllegalStateException("팀 정보가 모두 입력되지 않았습니다.");
        }

        return teams.stream().map(team -> {
            TeamResultDto dto = new TeamResultDto();

            dto.setTeamIndex(team.getTeamIndex());

            dto.setMembers(
                    team.getMembers().stream()
                            .map(member -> new MemberDto(member.getDepartment(), member.getName()))
                            .toList()
            );

            dto.setConscientiousnessSimilarityScore(getDouble(team.getConscientiousnessSimilarityScore()));
            dto.setConscientiousnessSimilarityEval(team.getConscientiousnessSimilarityEval());
            dto.setConscientiousnessMeanScore(getDouble(team.getConscientiousnessMeanScore()));
            dto.setConscientiousnessMeanEval(team.getConscientiousnessMeanEval());

            dto.setAgreeablenessSimilarityScore(getDouble(team.getAgreeablenessSimilarityScore()));
            dto.setAgreeablenessSimilarityEval(team.getAgreeablenessSimilarityEval());
            dto.setAgreeablenessMeanScore(getDouble(team.getAgreeablenessMeanScore()));
            dto.setAgreeablenessMeanEval(team.getAgreeablenessMeanEval());

            dto.setOpennessDiversityScore(getDouble(team.getOpennessDiversityScore()));
            dto.setOpennessDiversityEval(team.getOpennessDiversityEval());

            dto.setExtraversionDiversityScore(getDouble(team.getExtraversionDiversityScore()));
            dto.setExtraversionDiversityEval(team.getExtraversionDiversityEval());

            dto.setNeuroticismSimilarityScore(getDouble(team.getNeuroticismSimilarityScore()));
            dto.setNeuroticismSimilarityEval(team.getNeuroticismSimilarityEval());

            return dto;
        }).toList();
    }

    private double getDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0.0;
    }

}