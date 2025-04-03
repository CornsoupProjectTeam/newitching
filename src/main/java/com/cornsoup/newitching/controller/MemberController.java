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
        String memberId = memberService.registerMember(urlKey, request);
        return ResponseEntity.ok(Map.of("memberId", memberId));
    }
}