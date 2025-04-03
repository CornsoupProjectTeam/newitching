package com.cornsoup.newitching.controller;

import com.cornsoup.newitching.dto.MemberRegisterRequest;
import com.cornsoup.newitching.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matching")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/{urlKey}/member-register")
    public ResponseEntity<?> registerMember(
            @PathVariable String urlKey,
            @RequestBody MemberRegisterRequest request
    ) {
        boolean success = memberService.registerMember(urlKey, request);

        if (success) {
            return ResponseEntity.ok(Map.of("message", "등록이 완료되었습니다."));
        } else {
            return ResponseEntity.status(500).body(Map.of("message", "등록에 실패했습니다."));
        }
    }
}