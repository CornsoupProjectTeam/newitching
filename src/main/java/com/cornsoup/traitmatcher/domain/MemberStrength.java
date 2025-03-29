package com.cornsoup.traitmatcher.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "MEMBER_STRENGTH")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberStrength {
    @Id
    @Column(name = "MEMBER_STRENGTH_ID", length = 47)
    private String memberStrengthId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "STRENGTH", length = 100)
    private String strength;

    @Column(name = "STRENGTH_SCORE", precision = 4, scale = 2)
    private BigDecimal strengthScore;
}
