package com.lit.knowledgeforest.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lit.knowledgeforest.entity.HistoryEntity;
import com.lit.knowledgeforest.entity.primarykey.HistoryPK;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, HistoryPK>{
    Optional<HistoryEntity> findByPkEnoAndPkQuestionNo(String eno, Long questionNo);

    List<HistoryEntity> findAllByPkEnoAndCorrectYnOrderByLtChDttiDesc(String eno, String correctYn);

    @Query(
            value = """
 SELECT C.CATEGORY_NAME AS categoryName
  , COUNT(C.CATEGORY_NAME) AS SolvedCount
  , SUM(B.POINTS) AS TotalPoint
   FROM QZ_TB_HISTORY A
      , QZ_TB_QUESTION B
      , QZ_TB_CATEGORY C
  WHERE A.QUESTION_NO = B.QUESTION_NO
    AND B.QUESTION_NO = C.QUESTION_NO
    AND A.ENO = :eno
    AND A.CORRECT_YN = :correctYn
   GROUP BY C.CATEGORY_NAME
   ORDER BY SolvedCount DESC, TotalPoint DESC
 """,
            nativeQuery = true
    )
    List<Object[]> findAllMySolvedStatistic(@Param("eno") String eno, @Param("correctYn") String correctYn);
}
