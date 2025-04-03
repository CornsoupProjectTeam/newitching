package com.cornsoup.newitching.kafka;

import com.cornsoup.newitching.kafka.dto.Big5ScoreMessage;
import com.cornsoup.newitching.service.Big5ScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Big5ScoreConsumer {

    private final Big5ScoreService big5ScoreService;

    @KafkaListener(topics = "big5_scores", groupId = "springboot-group")
    public void consume(Big5ScoreMessage message) {
        log.info("BIG5 점수 수신: {}", message.getMemberId());
        big5ScoreService.updateBig5Scores(message);
    }
}