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
public class PointHistResponse {
    private String eno;

    private BigDecimal points;

    private String memo;

    private String fstCrtUsid;

    private String fstCrtDtti;

    private String ltChUsid;

    private String ltChDtti;
}
