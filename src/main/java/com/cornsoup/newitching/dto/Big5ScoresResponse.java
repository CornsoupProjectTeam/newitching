package com.cornsoup.newitching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Big5ScoresResponse {
    private BigDecimal conscientiousnessScore;
    private BigDecimal agreeablenessScore;
    private BigDecimal opennessScore;
    private BigDecimal extraversionScore;
    private BigDecimal neuroticismScore;
}
