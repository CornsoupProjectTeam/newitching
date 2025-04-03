package com.cornsoup.newitching.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMemberRegister(String memberId, String token) {
        Map<String, Object> message = Map.of(
                "token", token,
                "timestamp", DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        );

        kafkaTemplate.send("member_register", memberId, message);

        // 로그 출력
        log.info("Kafka 메시지 발행 완료 - memberId: {}", memberId);
    }
}