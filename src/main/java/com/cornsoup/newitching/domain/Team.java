package com.cornsoup.newitching.domain;

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

    @Column(name = "TEAM_INDEX")
    private Integer teamIndex;

    // 한 팀에 여러명의 멤버가 속할 수 있음
    // 팀이 삭제되더라도 멤버는 삭제되지 않음
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<Member> members;

    // 성실성
    @Column(name = "CONSCIENTIOUSNESS_SIMILARITY_SCORE", precision = 4, scale = 2)
    private BigDecimal conscientiousnessSimilarityScore;

    @Column(name = "CONSCIENTIOUSNESS_SIMILARITY_EVAL")
    private Integer conscientiousnessSimilarityEval;

    @Column(name = "CONSCIENTIOUSNESS_MEAN_SCORE", precision = 4, scale = 2)
    private BigDecimal conscientiousnessMeanScore;

    @Column(name = "CONSCIENTIOUSNESS_MEAN_EVAL")
    private Integer conscientiousnessMeanEval;

    // 친화성
    @Column(name = "AGREEABLENESS_SIMILARITY_SCORE", precision = 4, scale = 2)
    private BigDecimal agreeablenessSimilarityScore;

    @Column(name = "AGREEABLENESS_SIMILARITY_EVAL")
    private Integer agreeablenessSimilarityEval;

    @Column(name = "AGREEABLENESS_MEAN_SCORE", precision = 4, scale = 2)
    private BigDecimal agreeablenessMeanScore;

    @Column(name = "AGREEABLENESS_MEAN_EVAL")
    private Integer agreeablenessMeanEval;

    // 개방성
    @Column(name = "OPENNESS_DIVERSITY_SCORE", precision = 4, scale = 2)
    private BigDecimal opennessDiversityScore;

    @Column(name = "OPENNESS_DIVERSITY_EVAL")
    private Integer opennessDiversityEval;

    // 외향성
    @Column(name = "EXTRAVERSION_DIVERSITY_SCORE", precision = 4, scale = 2)
    private BigDecimal extraversionDiversityScore;

    @Column(name = "EXTRAVERSION_DIVERSITY_EVAL")
    private Integer extraversionDiversityEval;

    // 신경증
    @Column(name = "NEUROTICISM_SIMILARITY_SCORE", precision = 4, scale = 2)
    private BigDecimal neuroticismSimilarityScore;

    @Column(name = "NEUROTICISM_SIMILARITY_EVAL")
    private Integer neuroticismSimilarityEval;
}
