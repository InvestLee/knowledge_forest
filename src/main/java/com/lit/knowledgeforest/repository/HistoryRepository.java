package com.lit.knowledgeforest.repository;

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

    @Query(
            value = """
 SELECT C.ENO
  , C.QUESTION_NO
  , C.CORRECT_YN
  , DBMS_LOB.SUBSTR(C.CONTENT, 20, 1) AS CONTENT
  , C.TYPE_CD
  , C.LEVEL_CD
  , C.CATEGORY_NAME
  , C.POINTS
  , C.FST_CRT_USID
  , TO_CHAR(C.FST_CRT_DTTI, 'YYYY-MM-DD HH24:MI:SS') AS FST_CRT_DTTI
  , C.LT_CH_USID
  , TO_CHAR(C.LT_CH_DTTI, 'YYYY-MM-DD HH24:MI:SS') AS LT_CH_DTTI
 FROM (SELECT A.ENO
    , A.QUESTION_NO
    , A.CORRECT_YN
    , B.CONTENT
    , B.TYPE_CD
    , B.LEVEL_CD
    , B.CATEGORY_NAME
    , B.POINTS
    , A.FST_CRT_USID
    , A.FST_CRT_DTTI
    , A.LT_CH_USID
    , A.LT_CH_DTTI
       , ROW_NUMBER() OVER (ORDER BY A.LT_CH_DTTI DESC) AS rn
     FROM QZ_TB_HISTORY A
        , QZ_TB_QUESTION B 
       WHERE A.QUESTION_NO = B.QUESTION_NO
         AND A.ENO = :eno
       AND A.CORRECT_YN = :correctYn) C
 WHERE C.rn BETWEEN :startRow AND :endRow
 ORDER BY C.LT_CH_DTTI DESC
 """,
            nativeQuery = true
    )
    List<Object[]> findAllByPkEnoAndCorrectYnOrderByLtChDttiDesc(
            @Param("eno") String eno,
            @Param("correctYn") String correctYn,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow
    );

    int countByPkEnoAndCorrectYn(
            String eno,
            String correctYn
    );

    @Query(
            value = """
 SELECT categoryName, solvedCount, totalPoint
 FROM (SELECT B.CATEGORY_NAME AS categoryName
       , COUNT(B.CATEGORY_NAME) AS solvedCount
    , SUM(B.POINTS) AS totalPoint
       , ROW_NUMBER() OVER (ORDER BY SUM(B.POINTS) DESC, COUNT(B.CATEGORY_NAME) DESC) AS rn
     FROM QZ_TB_HISTORY A
           , QZ_TB_QUESTION B
       WHERE A.QUESTION_NO = B.QUESTION_NO
       AND A.ENO = :eno
       AND A.CORRECT_YN = :correctYn
       GROUP BY B.CATEGORY_NAME)
 """,
            nativeQuery = true
    )
    List<Object[]> findAllMySolvedStatistic(
            @Param("eno") String eno,
            @Param("correctYn") String correctYn
    );

    @Query(
            value = """
 SELECT COUNT(*) FROM (
 SELECT B.CATEGORY_NAME
   FROM QZ_TB_HISTORY A
      , QZ_TB_QUESTION B
  WHERE A.QUESTION_NO = B.QUESTION_NO
    AND A.ENO = :eno
    AND A.CORRECT_YN = :correctYn
  GROUP BY B.CATEGORY_NAME
  )
 """,
            nativeQuery = true
    )
    int countAllMySolvedStatistic(
            @Param("eno") String eno,
            @Param("correctYn") String correctYn
    );

    @Query(
            value = """
 SELECT TO_CHAR(B.DT, 'YYYYMMDD') AS solveDate
  , NVL(C.eno,'null') as eno
  , NVL(C.solvedCount,0) as solvedCount
   FROM (SELECT TRUNC(SYSDATE) - LEVEL + 1 AS DT
   FROM DUAL CONNECT BY LEVEL <= 365
    ) B
   ,(SELECT TRUNC(A.LT_CH_DTTI) AS solveDate
          , A.ENO AS eno
    , COUNT(A.ENO) AS solvedCount
          FROM QZ_TB_HISTORY A
    WHERE A.LT_CH_DTTI > SYSDATE-365
        AND A.ENO = :eno
        AND A.CORRECT_YN = :correctYn
     GROUP BY TRUNC(LT_CH_DTTI), A.ENO
   ) C
 WHERE B.DT = C.solveDate(+)
 ORDER BY solveDate
 """,
            nativeQuery = true
    )
    List<Object[]> findStreakYear(
            @Param("eno") String eno,
            @Param("correctYn") String correctYn
    );
}


