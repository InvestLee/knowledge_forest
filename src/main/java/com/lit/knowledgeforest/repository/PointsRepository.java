package com.lit.knowledgeforest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lit.knowledgeforest.entity.PointsEntity;

@Repository
public interface PointsRepository extends JpaRepository<PointsEntity, String>{

    @Query(
            value = """
 SELECT RANK() OVER (ORDER BY POINTS DESC) AS rank
      , ENO AS eno
  , POINTS AS points
   FROM QZ_TB_POINTS
  ORDER BY rank, eno
 """,
            nativeQuery = true
    )
    List<Object[]> findAllPointRank();

    //nativeQuery 단건 조회 시 Object[]에서 Object로 변경되는 버그 발생하여 List 형태로 추출
    @Query(
            value = """
 SELECT ranked.rank as rank, ranked.eno as eno, ranked.points as points
 FROM(
 SELECT RANK() OVER (ORDER BY POINTS DESC) AS rank
        , ENO AS eno
    , POINTS AS points
      FROM QZ_TB_POINTS
  ) ranked
  WHERE ranked.eno = :eno
 """,
            nativeQuery = true
    )
    List<Object[]> findMyRank(@Param("eno") String eno);
}
