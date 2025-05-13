package com.cornsoup.newitching.controller;

import com.cornsoup.newitching.dto.MemberRegisterRequest;
import com.cornsoup.newitching.service.MemberService;
import com.cornsoup.newitching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MatchingService matchingService;


    @GetMapping("/{urlKey}")
    public ResponseEntity<Map<String, String>> getMatchingIdByUrlKey(@PathVariable String urlKey) {
        String matchingId = matchingService.getMatchingIdByUrlKey(urlKey);
        return ResponseEntity.ok(Map.of("matchingId", matchingId));
    }

    // 멤버 등록
    @PostMapping("/{urlKey}/register")
    public ResponseEntity<Map<String, String>> registerMember(
            @PathVariable String urlKey,
            @RequestBody MemberRegisterRequest request
    ) {
        String token = memberService.registerMember(urlKey, request);
        return ResponseEntity.ok(Map.of(
                "message", "등록이 완료되었습니다.",
                "token", token
        ));
    }

    // 채팅 시작
    @GetMapping("/{urlKey}/chat")
    public ResponseEntity<Map<String, String>> getMatchingIdByUrlKeyForChat(@PathVariable String urlKey) {
        String matchingId = matchingService.getMatchingIdByUrlKey(urlKey);
        return ResponseEntity.ok(Map.of("matchingId", matchingId));
    }
}
