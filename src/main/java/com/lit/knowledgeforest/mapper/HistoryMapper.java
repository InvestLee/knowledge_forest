package com.lit.knowledgeforest.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.lit.knowledgeforest.dto.RankResponse;
import com.lit.knowledgeforest.dto.RecentSolvedResponse;

@Repository
@Mapper
public interface HistoryMapper {
    int getSolvedTodayQuizByEno(HashMap<String, String> map);

    List<RecentSolvedResponse> findSolvedQuestions(
            @Param("eno") String eno,
            @Param("correctYn") String correctYn,
            @Param("category") String category,
            @Param("lev") String lev,
            @Param("type") String type,
            @Param("keyword") String keyword,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow
    );

    int countSolvedQuestions(
            @Param("eno") String eno,
            @Param("correctYn") String correctYn,
            @Param("category") String category,
            @Param("lev") String lev,
            @Param("type") String type,
            @Param("keyword") String keyword
    );
}


