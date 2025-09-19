package com.lit.knowledgeforest.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lit.knowledgeforest.entity.QuestionEntity;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, BigDecimal>{

    List<QuestionEntity> findByStatus(String status);
    Page<QuestionEntity> findByStatus(String status, Pageable pageable);
    Page<QuestionEntity> findByStatusAndContentContaining(String status, String search, Pageable pageable);
    Page<QuestionEntity> findByStatusAndCategoryName(String status, String categoryName, Pageable pageable);
    Page<QuestionEntity> findByStatusAndCategoryNameAndContentContaining(String status, String categoryName, String search, Pageable pageable);
    List<QuestionEntity> findByStatusAndQuestionNo(String status, BigDecimal questionNo);

    @Query(value = """
 SELECT * FROM QZ_TB_QUESTION 
 where status = 'Y' 
 and lower(content) like '%'|| lower(:keyword) ||'%'
 """,
            nativeQuery = true)
    List<QuestionEntity> searchApprovedQuestionByKeyword(@Param("keyword") String keyword);

    @Query(value = """
 SELECT * 
   FROM QZ_TB_QUESTION A
  WHERE A.CATEGORY_NAME = :questionId
  AND A.LEVEL_CD = :levelId
    AND A.STATUS  = 'Y'
    AND NOT EXISTS (
                    SELECT 'Y'
                      FROM QZ_TB_HISTORY B
                     WHERE A.QUESTION_NO = B.QUESTION_NO
                       AND B.ENO = :eno
                       AND B.CORRECT_YN = 'Y'
                   )
 """,
            nativeQuery = true)
    List<QuestionEntity> findByQuestionIdAndLevelId(@Param("questionId") String questionId, @Param("levelId") String levelId , @Param("eno") String eno );

    @Query(value = "SELECT NVL(MAX(question_no), 0) + 1 FROM QZ_TB_QUESTION", nativeQuery = true)
    BigDecimal getNextQuestionNo();

    @Query(value = "SELECT * FROM QZ_TB_QUESTION WHERE QUESTION_NO = :questionId", nativeQuery = true)
    QuestionEntity getQuestionInfoById(@Param("questionId") BigDecimal questionId);
}
