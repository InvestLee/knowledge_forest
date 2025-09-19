package com.lit.knowledgeforest.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lit.knowledgeforest.entity.QuestionEntity;
import com.lit.knowledgeforest.entity.UserInfoEntity;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String>{

    @Query(
            value = """
 SELECT MAX(A.rank) as rank
      , A.eno as eno
      , MAX(A.points) as points
      , COUNT(B.eno) as solvedCount
 FROM (SELECT RANK() OVER (ORDER BY POINTS DESC) AS rank
          , ENO
      , POINTS
      , ROW_NUMBER() OVER (ORDER BY POINTS ASC, ENO ASC) AS rn
       FROM QZ_TB_USER_INFO
    ) A
    , QZ_TB_HISTORY B
  WHERE A.ENO = B.ENO
    AND B.CORRECT_YN = :correctYn
    AND A.rn BETWEEN :startRow AND :endRow
  GROUP BY A.eno
  ORDER BY rank, eno
 """,
            nativeQuery = true
    )
    List<Object[]> findAllPointRank(
            @Param("correctYn") String correctYn,
            @Param("StartRow") int startRow,
            @Param("endRow") int endRow
    );

    //nativeQuery 단건 조회 시 Object[]에서 Object로 변경되는 버그 발생하여 List 형태로 추출
    @Query(
            value = """
 SELECT MAX(A.rank) as rank
      , A.eno as eno
      , MAX(A.points) as points
      , COUNT(B.eno) as solvedCount
 FROM QZ_TB_HISTORY B
    , (
      SELECT RANK() OVER (ORDER BY POINTS DESC) AS rank
          , ENO AS eno
      , POINTS AS points
         FROM QZ_TB_USER_INFO
    ) A
  WHERE A.eno = :eno
    AND B.CORRECT_YN = :correctYn
  GROUP BY A.eno
 """,
            nativeQuery = true
    )
    List<Object[]> findMyRank(
            @Param("eno") String eno,
            @Param("correctYn") String correctYn
    );

    @Query(value = """
 SELECT * 
   FROM QZ_TB_USER_INFO A
  WHERE A.ENO = :eno
 """,
            nativeQuery = true)
    Optional<UserInfoEntity> findByCustomEno(@Param("eno") String eno );

    @Transactional
    @Modifying
    @Query(value = """
 UPDATE QZ_TB_USER_INFO A
    SET A.POINTS = A.POINTS + :points
  WHERE A.ENO = :eno
 """,
            nativeQuery = true)
    void updatePoints(@Param("eno") String eno, @Param("points") BigDecimal points );

}
