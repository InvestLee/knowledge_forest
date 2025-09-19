package com.lit.knowledgeforest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
public class Question {

    private BigDecimal questionNo;

    private String content;

    private String description;

    private String answer;

    private String typeCd;

    private String typeName;

    private String levelCd;

    private String levelName;

    private String categoryName;

    private BigDecimal points;

    private String fstCrtUsid;

    private LocalDateTime fstCrtDtti;

    private String rejectCn;

    private List<QuizChoice> choices;

 /*
 public Question(QuestionEntity q) {
 this.questionNo = q.getQuestionNo();
 this.content = q.getContent();
 this.description = q.getDescription();
 this.answer = q.getAnswer();
 this.typeCd = q.getTypeCd();
 this.levelCd = q.getLevelCd();
 this.categoryName = q.getCategoryName();
 this.points = q.getPoints();
 this.fstCrtUsid = q.getFstCrtUsid();
 this.fstCrtDtti = q.getFstCrtDtti();
 this.rejectCn = q.getRejectCn();
 }
 */

    @Override
    public String toString() {
        return "Question [questionNo=" + questionNo + ", content=" + content + ", description=" + description
                + ", answer=" + answer + ", typeCd=" + typeCd + ", levelCd=" + levelCd + ", categoryName="
                + categoryName + ", points=" + points + ", fstCrtUsid=" + fstCrtUsid + ", fstCrtDtti=" + fstCrtDtti
                + ", rejectCn=" + rejectCn + "]";
    }


}