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
public class RecentSolvedRequest {

    private String eno;

    private String type;

    private String lev;

    private String category;

    private String keyword;

    private int page;

    private int size;
}