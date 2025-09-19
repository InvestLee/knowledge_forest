package com.lit.knowledgeforest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StreakResponse {

    private String solveDate;

    private String eno;

    private BigDecimal solvedCount;
}