package com.cornsoup.newitching.kafka.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class TeamResultMessage {
    private String matchingId;
    private List<TeamResultMessageDto> teams;
}