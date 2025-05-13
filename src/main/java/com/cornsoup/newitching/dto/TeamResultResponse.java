package com.cornsoup.newitching.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamResultResponse {
    private int teamSize;
    private int memberCount;
    private List<TeamResultDto> results;
}