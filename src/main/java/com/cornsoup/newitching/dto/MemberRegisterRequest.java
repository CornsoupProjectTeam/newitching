package com.cornsoup.newitching.dto;

import lombok.Getter;
import lombok.Setter;

// 클라이언트가 이 DTO 형식으로 JSON을 보내게 됨
@Getter
@Setter
public class MemberRegisterRequest {
    private String department;
    private String name;
}