package com.lit.knowledgeforest.dto;

import java.math.BigDecimal;

import com.lit.knowledgeforest.entity.QuestionEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {

    private BigDecimal questionNo;

    private String content;

    private String description;

    private String answer;

    private String typeCd;

    private String levelCd;

    private String categoryName;

    private BigDecimal points;


    public Quiz(QuestionEntity q) {
        this.questionNo = q.getQuestionNo();
        this.content = q.getContent();
        this.description = q.getDescription();
        this.answer = q.getAnswer();
        this.typeCd = q.getTypeCd();
        this.levelCd = q.getLevelCd();
        this.categoryName = q.getCategoryName();
        this.points = q.getPoints();
    }
}