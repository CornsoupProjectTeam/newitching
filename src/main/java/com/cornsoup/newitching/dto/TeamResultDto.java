package com.cornsoup.newitching.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class TeamResultDto {
    private String teamId;
    private BigDecimal conscientiousnessSimilarityScore;
    private BigDecimal conscientiousnessSimilarityEval;
    private BigDecimal conscientiousnessMeanScore;
    private BigDecimal conscientiousnessMeanEval;
    private BigDecimal agreeablenessSimilarityScore;
    private BigDecimal agreeablenessSimilarityEval;
    private BigDecimal agreeablenessMeanScore;
    private BigDecimal agreeablenessMeanEval;
    private BigDecimal opennessDiversityEval;
    private BigDecimal extraversionDiversityEval;
    private BigDecimal neuroticismSimilarityScore;
    private BigDecimal neuroticismSimilarityEval;
    private List<String> memberNames; // 간단하게 멤버 이름만 추출
}

