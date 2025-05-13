package com.cornsoup.newitching.repository;

import com.cornsoup.newitching.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, String> {

    @Query("""
    SELECT DISTINCT m.team FROM Member m
    JOIN FETCH m.team.members tm
    WHERE m.matching.matchingId = :matchingId AND m.team IS NOT NULL
    """)
    List<Team> findTeamsByMatchingId(@Param("matchingId") String matchingId);

}
