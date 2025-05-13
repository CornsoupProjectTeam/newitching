package com.cornsoup.newitching.kafka;

import com.cornsoup.newitching.kafka.dto.TeamResultMessage;
import com.cornsoup.newitching.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamMatchingResultConsumer {

    private final TeamService teamService;

    @KafkaListener(
            topics = "team_matching_results",
            groupId = "springboot-group",
            properties = {
                    "spring.json.value.default.type=com.cornsoup.newitching.kafka.dto.TeamResultMessage",
                    "spring.json.trusted.packages=com.cornsoup.newitching.kafka.dto"
            }
    )
    @KafkaListener(topics = "team_matching_results", groupId = "springboot-group")
    public void consume(TeamResultMessage message) {
        log.info("Team matching result received - matchingId: {}", message.getMatchingId());
        teamService.saveMatchingResults(message);
    }
}
