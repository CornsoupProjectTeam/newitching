package com.cornsoup.newitching.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MATCHING_INFO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingInfo {

    @Id
    @Column(name = "MATCHING_ID", length = 20)
    private String matchingId;

    @Column(name = "PASSWORD", length = 255)
    private String password;

    @Column(name = "MEMBER_COUNT")
    private Integer memberCount;

    @Column(name = "TEAM_SIZE")
    private Integer teamSize;

    @Column(name = "DEADLINE")
    private LocalDateTime deadline;

    @Column(name = "URL", length = 255)
    private String url;

    @OneToMany(mappedBy = "matching", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Member> members;

}
