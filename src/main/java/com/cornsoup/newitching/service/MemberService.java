package com.cornsoup.newitching.service;

import com.cornsoup.newitching.domain.MatchingInfo;
import com.cornsoup.newitching.domain.Member;
import com.cornsoup.newitching.dto.MemberRegisterRequest;
import com.cornsoup.newitching.kafka.dto.Big5ScoreMessage;
import com.cornsoup.newitching.repository.MatchingInfoRepository;
import com.cornsoup.newitching.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MatchingInfoRepository matchingInfoRepository;
    private final JwtService jwtService;
    private final MatchingService matchingService;

    public String registerMember(String urlKey, MemberRegisterRequest request) {
        // 1. URL로 매칭 조회
        MatchingInfo matchingInfo = matchingInfoRepository.findByUrl(urlKey)
                .orElseThrow(() -> new IllegalArgumentException("해당 URL에 해당하는 매칭 정보가 없습니다."));

        // 2. 해당 매칭에 부서 + 이름이 동일한 사람 있는지 검사
        boolean isDuplicate = memberRepository.existsByMatchingAndDepartmentAndName(
                matchingInfo, request.getDepartment(), request.getName()
        );
        if (isDuplicate) {
            throw new IllegalArgumentException("이미 등록된 부서/이름입니다.");
        }

        // 3. UUID로 MemberId 생성
        String memberId = UUID.randomUUID().toString();

        // 4. DB에 저장
        Member member = Member.builder()
                .memberId(memberId)
                .name(request.getName())
                .department(request.getDepartment())
                .matching(matchingInfo)
                .build();
        memberRepository.save(member);
        log.info("멤버 등록 완료 - memberId: {}", memberId);

        // 5. 토큰 즉시 생성
        String token = jwtService.createToken(memberId);
        log.info("토큰 발급 완료 - memberId: {}", memberId);

        return token;
    }

    @Transactional
    public void updateBig5Scores(Big5ScoreMessage message) {
        Member member = memberRepository.findById(message.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));

        Map<String, Double> scores = message.getScores();

        member.setConscientiousnessScore(toBigDecimal(scores.get("성실성")));
        member.setAgreeablenessScore(toBigDecimal(scores.get("친화성")));
        member.setOpennessScore(toBigDecimal(scores.get("경험에 대한 개방성")));
        member.setExtraversionScore(toBigDecimal(scores.get("외향성")));
        member.setNeuroticismScore(toBigDecimal(scores.get("신경증")));

        memberRepository.save(member);
        log.info("BIG5 점수 저장 완료 - memberId: {}", message.getMemberId());

        // Big5 점수 업데이트 후 매칭 모니터링 트리거
        matchingService.monitorMatching(member.getMatching().getMatchingId());
    }

    private BigDecimal toBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }
}

