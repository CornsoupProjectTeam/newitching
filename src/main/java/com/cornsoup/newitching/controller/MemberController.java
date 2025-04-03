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
        try {
            String token = memberService.registerMember(urlKey, request);
            return ResponseEntity.ok(Map.of(
                    "message", "등록이 완료되었습니다.",
                    "token", token
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "서버 오류로 등록에 실패했습니다."));
        }
    }
}