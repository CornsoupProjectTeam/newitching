package com.cornsoup.newitching.service;

import com.cornsoup.newitching.domain.MatchingInfo;
import com.cornsoup.newitching.domain.Member;
import com.cornsoup.newitching.domain.Team;
import com.cornsoup.newitching.kafka.dto.TeamResultMessage;
import com.cornsoup.newitching.kafka.dto.TeamResultMessageDto;
import com.cornsoup.newitching.repository.MatchingInfoRepository;
import com.cornsoup.newitching.repository.MemberRepository;
import com.cornsoup.newitching.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final MatchingInfoRepository matchingInfoRepository;

    @Transactional
    public void saveMatchingResults(TeamResultMessage message) {
        String matchingId = message.getMatchingId();

        MatchingInfo matchingInfo = matchingInfoRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매칭 ID가 존재하지 않습니다."));

        for (TeamResultMessageDto teamDto : message.getTeams()) {
            // 1. 팀 엔티티 생성
            Team team = Team.builder()
                    .teamId(UUID.randomUUID().toString())
                    .teamIndex(teamDto.getTeamIndex())
                    .conscientiousnessSimilarityScore(toBigDecimal(teamDto.getConscientiousnessSimilarityScore()))
                    .conscientiousnessSimilarityEval(teamDto.getConscientiousnessSimilarityEval())
                    .conscientiousnessMeanScore(toBigDecimal(teamDto.getConscientiousnessMeanScore()))
                    .conscientiousnessMeanEval(teamDto.getConscientiousnessMeanEval())
                    .agreeablenessSimilarityScore(toBigDecimal(teamDto.getAgreeablenessSimilarityScore()))
                    .agreeablenessSimilarityEval(teamDto.getAgreeablenessSimilarityEval())
                    .agreeablenessMeanScore(toBigDecimal(teamDto.getAgreeablenessMeanScore()))
                    .agreeablenessMeanEval(teamDto.getAgreeablenessMeanEval())
                    .opennessDiversityScore(toBigDecimal(teamDto.getOpennessDiversityScore()))
                    .opennessDiversityEval(teamDto.getOpennessDiversityEval())
                    .extraversionDiversityScore(toBigDecimal(teamDto.getExtraversionDiversityScore()))
                    .extraversionDiversityEval(teamDto.getExtraversionDiversityEval())
                    .neuroticismSimilarityScore(toBigDecimal(teamDto.getNeuroticismSimilarityScore()))
                    .neuroticismSimilarityEval(teamDto.getNeuroticismSimilarityEval())
                    .build();

            teamRepository.save(team);

            // 2. 멤버 할당
            List<Member> members = memberRepository.findAllById(teamDto.getMemberIds());
            for (Member member : members) {
                member.setTeam(team);
            }
            memberRepository.saveAll(members);

            log.info("Team saved successfully - teamId: {}", team.getTeamId());
        }
    }

    private BigDecimal toBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }
}
