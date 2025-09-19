package com.lit.knowledgeforest.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lit.knowledgeforest.entity.PointHistoryEntity;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistoryEntity, String>{
    Page<PointHistoryEntity> findByEno(String eno, Pageable pageable);

    @Query(
            value = """
 SELECT C.ENO
  , C.POINTS
  , C.MEMO
  , C.FST_CRT_USID
  , TO_CHAR(C.FST_CRT_DTTI, 'YYYY-MM-DD HH24:MI:SS') AS FST_CRT_DTTI
  , C.LT_CH_USID
  , TO_CHAR(C.LT_CH_DTTI, 'YYYY-MM-DD HH24:MI:SS') AS LT_CH_DTTI
 FROM (SELECT A.ENO
    , A.POINTS
    , A.MEMO
    , A.FST_CRT_USID
    , A.FST_CRT_DTTI
    , A.LT_CH_USID
    , A.LT_CH_DTTI
       , ROW_NUMBER() OVER (ORDER BY A.LT_CH_DTTI DESC) AS rn
     FROM QZ_TB_POINT_HIST A 
       WHERE A.ENO = :eno) C
 WHERE C.rn BETWEEN :startRow AND :endRow
 ORDER BY C.LT_CH_DTTI DESC
 """,
            nativeQuery = true
    )
    List<Object[]> findAllByEnoOrderByLtChDttiDesc(
            @Param("eno") String eno,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow
    );

    int countByEno(
            String eno
    );
}
