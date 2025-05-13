package com.cornsoup.newitching.controller;

import com.cornsoup.newitching.dto.MatchingRegisterRequest;
import com.cornsoup.newitching.dto.MemberRegisterRequest;
import com.cornsoup.newitching.dto.TeamResultDto;
import com.cornsoup.newitching.dto.TeamResultResponse;
import com.cornsoup.newitching.service.MatchingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matching")
public class MatchingController {

    private final MatchingService matchingService;

    // 프로젝트 ID 중복 확인
    @PostMapping("/doublecheck")
    public ResponseEntity<Map<String, String>> doubleCheckMatchingId(@RequestBody Map<String, String> body) {
        String matchingId = body.get("matchingId");
        matchingService.doubleCheckMatchingId(matchingId);
        return ResponseEntity.ok(Map.of("message", "사용 가능한 매칭 ID입니다."));
    }

    // 매칭 등록
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerMatching(@RequestBody MatchingRegisterRequest request) {
        String urlkey = matchingService.registerMatching(request);
        return ResponseEntity.ok(Map.of("urlkey", urlkey));
    }

    // 매칭 결과 조회
    @PostMapping("/results")
    public ResponseEntity<TeamResultResponse> getMatchingResults(@RequestBody Map<String, String> body) {
        String matchingId = body.get("matchingId");
        String password = body.get("password");

        matchingService.validateMatchingIdAndPassword(matchingId, password);

        List<TeamResultDto> results = matchingService.getMatchingResults(matchingId);
        int teamSize = matchingService.getTeamSize(matchingId);
        int memberCount = matchingService.getMemberCount(matchingId);

        TeamResultResponse response = new TeamResultResponse(teamSize, memberCount, results);
        return ResponseEntity.ok(response);
    }

}
