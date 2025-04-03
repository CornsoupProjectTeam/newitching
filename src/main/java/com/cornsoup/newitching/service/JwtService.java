package com.cornsoup.newitching.service;

import com.cornsoup.newitching.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;

    public String createToken(String memberId) {
        return jwtTokenProvider.generateToken(memberId);
    }
}
