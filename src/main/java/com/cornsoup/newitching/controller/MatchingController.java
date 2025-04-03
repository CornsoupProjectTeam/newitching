package com.cornsoup.newitching.controller;

import com.cornsoup.newitching.dto.MatchingRegisterRequest;
import com.cornsoup.newitching.dto.TeamResultDto;
import com.cornsoup.newitching.service.MatchingService;
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

    // 매칭 등록
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerMatching(@RequestBody MatchingRegisterRequest request) {
        String url = matchingService.registerMatching(request);
        Map<String, String> response = new HashMap<>();
        response.put("url", url);
        return ResponseEntity.ok(response);
    }

    // 매칭 결과 조회
    @GetMapping("/{matchingId}/results")
    public ResponseEntity<List<TeamResultDto>> getMatchingResults(@PathVariable String matchingId) {
        List<TeamResultDto> results = matchingService.getMatchingResults(matchingId);
        return ResponseEntity.ok(results);
    }
}
