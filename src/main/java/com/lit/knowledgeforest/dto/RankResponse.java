package com.lit.knowledgeforest.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankResponse {

    private BigDecimal rank;

    private String eno;

    private BigDecimal points;
}