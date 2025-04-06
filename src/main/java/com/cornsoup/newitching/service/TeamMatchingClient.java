package com.cornsoup.newitching.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMatchingClient {

    private final WebClient webClient;

    public void requestTeamMatching(String matchingId, String jwtToken) {
        webClient.post()
                .uri("/start")
                .header("Authorization", "Bearer " + jwtToken)
                .exchangeToMono(response -> handleResponse(response, matchingId))
                .retryWhen(
                        Retry.fixedDelay(3, Duration.ofSeconds(5)) // 5초 간격으로 최대 3번 재시도
                                .filter(this::isRetryable)
                                .onRetryExhaustedThrow((retrySpec, signal) -> signal.failure())
                )
                .subscribe(
                        success -> {},
                        error -> log.error("최종 매칭 요청 실패 - matchingId: {}, error: {}", matchingId, error.getMessage())
                );
    }

    private Mono<Void> handleResponse(ClientResponse response, String matchingId) {
        if (response.statusCode().is2xxSuccessful()) {
            log.info("팀 매칭 요청 성공 - matchingId: {}", matchingId);
            return Mono.empty();
        } else {
            log.warn("팀 매칭 요청 실패 - matchingId: {}, 상태코드: {}", matchingId, response.statusCode());
            return Mono.error(new RuntimeException("팀 매칭 요청 실패 - 상태코드: " + response.statusCode()));
        }
    }

    private boolean isRetryable(Throwable throwable) {
        // 네트워크 오류나 서버 내부 오류일 때만 재시도, 클라이언트가 잘못 요청한 경우에는 재시도 X
        return !(throwable instanceof IllegalArgumentException);
    }
}
