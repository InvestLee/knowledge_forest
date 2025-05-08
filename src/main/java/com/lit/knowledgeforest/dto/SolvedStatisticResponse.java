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
public class SolvedStatisticResponse {

    private String categoryName;

    private BigDecimal SolvedCount;

    private BigDecimal TotalPoint;
}
