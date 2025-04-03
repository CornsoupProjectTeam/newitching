package com.cornsoup.newitching.service;

import com.cornsoup.newitching.kafka.MemberKafkaProducer;
import com.cornsoup.newitching.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberKafkaProducer memberKafkaProducer;

    @Async
    public void createTokenAsync(String memberId) {
        String token = jwtTokenProvider.generateToken(memberId);
        log.info("JWT 토큰 생성 완료 - memberId: {}", memberId);

        memberKafkaProducer.sendMemberRegister(memberId, token);
        log.info("Kafka 메시지 발행 요청 완료 - memberId: {}", memberId);
    }
}
