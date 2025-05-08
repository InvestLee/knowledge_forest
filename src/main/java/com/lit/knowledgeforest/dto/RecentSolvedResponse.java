package com.lit.knowledgeforest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentSolvedResponse {

    private String eno;

    private BigDecimal questionNo;

    private String correctYn;

    protected String fstCrtUsid;

    protected LocalDateTime fstCrtDtti;

    protected String ltChUsid;

    protected LocalDateTime ltChDtti;
}
