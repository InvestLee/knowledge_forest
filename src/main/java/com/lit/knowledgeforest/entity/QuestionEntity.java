package com.lit.knowledgeforest.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "qz_tb_question")
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class QuestionEntity extends BaseEntity{

    @Id
    @Column(name="QUESTION_NO")
    private BigDecimal questionNo;

    @Lob
    private String content;

    @Lob
    private String description;

    private String answer;

    @Column(name="TYPE_CD")
    private String typeCd;

    @Column(name="LEVEL_CD")
    private String levelCd;

    private String status;

    private BigDecimal points;

    @Column(name="CATEGORY_NAME")
    private String categoryName;

    @Column(name="REJECT_CN")
    private String rejectCn;
}