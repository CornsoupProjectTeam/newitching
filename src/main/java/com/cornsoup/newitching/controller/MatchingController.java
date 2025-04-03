package com.cornsoup.newitching.controller;

import com.cornsoup.newitching.dto.MatchingRegisterRequest;
import com.cornsoup.newitching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matching-register")
public class MatchingController {

    private final MatchingService matchingService;

    @PostMapping
    public ResponseEntity<Map<String, String>> registerMatching(@RequestBody MatchingRegisterRequest request) {
        String url = matchingService.registerMatching(request);
        Map<String, String> response = new HashMap<>();
        response.put("url", url);
        return ResponseEntity.ok(response);
    }
}
