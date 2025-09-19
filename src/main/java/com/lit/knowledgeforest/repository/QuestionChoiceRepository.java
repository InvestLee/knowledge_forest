package com.lit.knowledgeforest.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lit.knowledgeforest.entity.QuestionChoiceEntity;
import com.lit.knowledgeforest.entity.primarykey.QuestionChoicePK;

@Repository
public interface QuestionChoiceRepository extends JpaRepository<QuestionChoiceEntity, QuestionChoicePK>{

    List<QuestionChoiceEntity> findByPkQuestionNoOrderByPkChoiceNo(BigDecimal questionNo);
    int deleteByPkQuestionNo(BigDecimal questionNo);
}
