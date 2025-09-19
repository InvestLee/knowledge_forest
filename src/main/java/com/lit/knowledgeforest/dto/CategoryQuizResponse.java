package com.lit.knowledgeforest.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryQuizResponse {

    private String eno;

    private BigDecimal questionNo;

    private String contentKeyword;

    private String correctYn;

    private String typeCd;

    private String levelCd;

    private String categoryName;

    private BigDecimal Points;
}