package com.cornsoup.newitching.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class TeamResultDto {
    private int teamIndex;
    private List<MemberDto> members;

    private double conscientiousnessSimilarityScore;
    private int conscientiousnessSimilarityEval;
    private double conscientiousnessMeanScore;
    private int conscientiousnessMeanEval;

    private double agreeablenessSimilarityScore;
    private int agreeablenessSimilarityEval;
    private double agreeablenessMeanScore;
    private int agreeablenessMeanEval;

    private double opennessDiversityScore;
    private int opennessDiversityEval;

    private double extraversionDiversityScore;
    private int extraversionDiversityEval;

    private double neuroticismSimilarityScore;
    private int neuroticismSimilarityEval;
}

