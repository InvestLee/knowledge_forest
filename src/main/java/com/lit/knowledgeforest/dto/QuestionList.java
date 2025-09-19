package com.lit.knowledgeforest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.lit.knowledgeforest.entity.QuestionEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionList {


    private BigDecimal questionNo;

    private String content;

    private String typeCd;

    private String levelCd;

    private String categoryName;

    private String fstCrtUsid;

    private LocalDateTime fstCrtDtti;

    public QuestionList(QuestionEntity q) {
        this.questionNo = q.getQuestionNo();
        this.content = q.getContent();
        this.typeCd = q.getTypeCd();
        this.levelCd = q.getLevelCd();
        this.categoryName = q.getCategoryName();
        this.fstCrtUsid = q.getFstCrtUsid();
        this.fstCrtDtti = q.getFstCrtDtti();
    }
}