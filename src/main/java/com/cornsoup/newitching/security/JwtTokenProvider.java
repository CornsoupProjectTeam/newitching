package com.cornsoup.newitching.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private Key key;

    private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1시간

    @PostConstruct
    public void init() {
        // 환경변수에서 SECRET_KEY 가져오기
        String secretKeyPlain = System.getenv("SECRET_KEY");

        if (secretKeyPlain == null || secretKeyPlain.length() < 32) {
            throw new IllegalArgumentException("환경변수 SECRET_KEY는 반드시 존재하고, 32자 이상이어야 합니다.");
        }

        this.key = Keys.hmacShaKeyFor(secretKeyPlain.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String memberId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 마이크로 서비스(traitmatcher)와 통신 시 인증에 사용될 토큰 발급
    public String generateServerToServerToken(String matchingId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 5 * 60 * 1000); // 5분

        return Jwts.builder()
                .setSubject("server-to-server")        // sub: "server-to-server"
                .claim("matching_id", matchingId)      // ✅ matching_id를 추가
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


}
