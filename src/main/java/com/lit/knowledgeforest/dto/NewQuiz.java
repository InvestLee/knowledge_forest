package com.lit.knowledgeforest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
public class NewQuiz {

    //private BigDecimal questionNo;

    private String content;

    private String description;

    private String answer;

    private String typeCd;

    private String levelCd;

    private String categoryName;

    private BigDecimal points;

    private List<QuizChoice> choices;

    //private String fstCrtUsid;

    //private String ltChUsid;

    //private LocalDateTime fstCrtDtti;

 /*
 @Override
 public String toString() {
 return "NewQuiz [questionNo=" + questionNo + ", content=" + content + ", description=" + description
 + ", answer=" + answer + ", typeCd=" + typeCd + ", levelCd=" + levelCd + ", categoryName="
 + categoryName + ", points=" + points + ", fstCrtUsid=" + fstCrtUsid + ", fstCrtDtti=" + fstCrtDtti
 + "]";
 }
 */


}