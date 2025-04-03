package com.cornsoup.traitmatcher.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 클라이언트가 이 DTO 형식으로 JSON을 보내게 됨
@Data
public class MatchingRegisterRequest {
    private String matchingId;
    private String password;
    private Integer memberCount;
    private Integer teamSize;
    private LocalDateTime deadline;
}
