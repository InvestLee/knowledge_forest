package com.lit.knowledgeforest.dto;

import java.math.BigDecimal;

import com.lit.knowledgeforest.code.Level;
import com.lit.knowledgeforest.code.QuestionType;

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

    private String content;

    private String type;

    private String lev;

    private String categoryName;

    private BigDecimal points;

    private String fstCrtUsid;

    private String fstCrtDtti;

    private String ltChUsid;

    private String ltChDtti;

    public void toEnum(String lev, String type) {
        this.lev = Level.getNameByCode(lev);
        this.type = QuestionType.getNameByCode(type);
    }
}