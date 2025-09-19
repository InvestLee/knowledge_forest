package com.lit.knowledgeforest.dto;

import java.math.BigDecimal;

import com.lit.knowledgeforest.entity.QuestionChoiceEntity;

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
public class QuizChoice {

    private BigDecimal questionNo;

    private BigDecimal choiceNo;

    private String content;


    public QuizChoice(QuestionChoiceEntity q) {
        this.questionNo = q.getPk().getQuestionNo();
        this.choiceNo = q.getPk().getChoiceNo();
        this.content = q.getContent();
    }
}