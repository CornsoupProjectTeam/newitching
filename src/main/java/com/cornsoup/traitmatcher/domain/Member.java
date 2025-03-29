package com.cornsoup.traitmatcher.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @Column(name = "MEMBER_ID", length = 41)
    private String memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID", nullable = true) // 팀이 없는 멤버를 허용
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MATCHING_ID", nullable = false)
    private MatchingInfo matching;

    @Column(name = "DEPARTMENT", length = 100)
    private String department;

    @Column(name = "NAME", length = 30)
    private String name;

    @Column(name = "CONSCIENTIOUSNESS_SCORE", precision = 4, scale = 2)
    private BigDecimal conscientiousnessScore;

    @Column(name = "AGREEABLENESS_SCORE", precision = 4, scale = 2)
    private BigDecimal agreeablenessScore;

    @Column(name = "OPENNESS_SCORE", precision = 4, scale = 2)
    private BigDecimal opennessScore;

    @Column(name = "EXTRAVERSION_SCORE", precision = 4, scale = 2)
    private BigDecimal extraversionScore;

    @Column(name = "NEUROTICISM_SCORE", precision = 4, scale = 2)
    private BigDecimal neuroticismScore;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MemberStrength> strengths;

}

