package com.cornsoup.newitching.kafka;

import com.cornsoup.newitching.kafka.dto.Big5ScoreMessage;
import com.cornsoup.newitching.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Big5ScoreConsumer {

    private final MemberService memberService;

    @KafkaListener(topics = "big5_scores", groupId = "springboot-group")
    public void consume(Big5ScoreMessage message) {
        log.info("BIG5 점수 수신: {}", message.getMemberId());
        memberService.updateBig5Scores(message);
    }
}