package com.cornsoup.newitching.service;


import com.cornsoup.newitching.domain.Member;
import com.cornsoup.newitching.kafka.dto.Big5ScoreMessage;
import com.cornsoup.newitching.repository.MemberRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Big5ScoreService {

    private final MemberRepository memberRepository;

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
    }

    private BigDecimal toBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }
}

