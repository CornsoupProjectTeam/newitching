package com.cornsoup.newitching.repository;

import com.cornsoup.newitching.domain.MatchingInfo;
import com.cornsoup.newitching.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByMatchingAndDepartmentAndName(MatchingInfo matching, String department, String name);

    // 특정 매칭에 등록된 전체 멤버 수
    long countByMatching_MatchingId(String matchingId);

    // Big5 점수가 모두 입력된 멤버만 체크
    @Query("SELECT COUNT(m) FROM Member m WHERE m.matching = :matching AND " +
            "(m.conscientiousnessScore IS NULL OR m.agreeablenessScore IS NULL OR " +
            "m.opennessScore IS NULL OR m.extraversionScore IS NULL OR m.neuroticismScore IS NULL)")
    long countIncompleteBig5MembersById(@Param("matchingId") String matchingId);

}
