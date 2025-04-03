package com.cornsoup.newitching.repository;

import com.cornsoup.newitching.domain.MatchingInfo;
import com.cornsoup.newitching.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByMatchingAndDepartmentAndName(MatchingInfo matching, String department, String name);

}
