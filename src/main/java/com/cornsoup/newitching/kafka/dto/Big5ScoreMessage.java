package com.cornsoup.newitching.kafka.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Big5ScoreMessage {
    private String memberId;
    private Map<String, Double> scores;
    private String timestamp;
}