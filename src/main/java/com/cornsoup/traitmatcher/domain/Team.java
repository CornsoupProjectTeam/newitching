package com.cornsoup.traitmatcher.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "TEAM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {
    @Id
    @Column(name = "TEAM_ID", length = 42)
    private String teamId;

    // 한 팀에 여러명의 멤버가 속할 수 있음
    // 팀이 삭제되더라도 멤버는 삭제되지 않음
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<Member> members;

    @Column(name = "CONSCIENTIOUSNESS_SIMILARITY_SCORE", precision = 4, scale = 2)
    private BigDecimal conscientiousnessSimilarityScore;

    @Column(name = "CONSCIENTIOUSNESS_SIMILARITY_EVAL", precision = 4, scale = 2)
    private BigDecimal conscientiousnessSimilarityEval;

    @Column(name = "CONSCIENTIOUSNESS_MEAN_SCORE", precision = 4, scale = 2)
    private BigDecimal conscientiousnessMeanScore;

    @Column(name = "CONSCIENTIOUSNESS_MEAN_EVAL", precision = 4, scale = 2)
    private BigDecimal conscientiousnessMeanEval;

    @Column(name = "AGREEABLENESS_SIMILARITY_SCORE", precision = 4, scale = 2)
    private BigDecimal agreeablenessSimilarityScore;

    @Column(name = "AGREEABLENESS_SIMILARITY_EVAL", precision = 4, scale = 2)
    private BigDecimal agreeablenessSimilarityEval;

    @Column(name = "AGREEABLENESS_MEAN_SCORE", precision = 4, scale = 2)
    private BigDecimal agreeablenessMeanScore;

    @Column(name = "AGREEABLENESS_MEAN_EVAL", precision = 4, scale = 2)
    private BigDecimal agreeablenessMeanEval;

    @Column(name = "OPENNESS_DIVERSITY_EVAL", precision = 4, scale = 2)
    private BigDecimal opennessDiversityEval;

    @Column(name = "EXTRAVERSION_DIVERSITY_EVAL", precision = 4, scale = 2)
    private BigDecimal extraversionDiversityEval;

    @Column(name = "NEUROTICISM_SIMILARITY_SCORE", precision = 4, scale = 2)
    private BigDecimal neuroticismSimilarityScore;

    @Column(name = "NEUROTICISM_SIMILARITY_EVAL", precision = 4, scale = 2)
    private BigDecimal neuroticismSimilarityEval;
}
