package com.cornsoup.newitching.service;

import com.cornsoup.newitching.domain.MatchingInfo;
import com.cornsoup.newitching.domain.Member;
import com.cornsoup.newitching.dto.MemberRegisterRequest;
import com.cornsoup.newitching.repository.MatchingInfoRepository;
import com.cornsoup.newitching.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MatchingInfoRepository matchingInfoRepository;
    private final JwtService jwtService;

    public boolean registerMember(String urlKey, MemberRegisterRequest request) {
        String url = "/matching/" + urlKey;

        // 1. URL로 매칭 조회
        MatchingInfo matchingInfo = matchingInfoRepository.findByUrl(url)
                .orElseThrow(() -> new IllegalArgumentException("해당 URL에 해당하는 매칭 정보가 없습니다."));

        // 2. 해당 매칭에 부서 + 이름이 동일한 사람 있는지 검사
        boolean isDuplicate = memberRepository.existsByMatchingAndDepartmentAndName(
                matchingInfo, request.getDepartment(), request.getName()
        );
        if (isDuplicate) {
            throw new IllegalArgumentException("이미 등록된 부서/이름입니다.");
        }

        // 3. UUID로 MemberId 생성
        String memberId = UUID.randomUUID().toString();

        // 4. DB에 저장
        Member member = Member.builder()
                .memberId(memberId)
                .name(request.getName())
                .department(request.getDepartment())
                .matching(matchingInfo)
                .build();
        memberRepository.save(member);

        // 5. 비동기로 토큰 발급 요청
        jwtService.createTokenAsync(memberId);
        log.info("토큰 발급 요청 완료 - memberId: {}", memberId);

        return true;
    }

}
