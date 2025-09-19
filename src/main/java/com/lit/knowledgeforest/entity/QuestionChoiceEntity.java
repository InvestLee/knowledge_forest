package com.lit.knowledgeforest.entity;

import com.lit.knowledgeforest.entity.primarykey.QuestionChoicePK;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "qz_tb_question_choice")
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class QuestionChoiceEntity extends BaseEntity{

    @EmbeddedId
    private QuestionChoicePK pk;

    @Lob
    private String content;
}
