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
public interface UserInfoMapper {

    List<RankResponse> findAllPointRank(
            @Param("cgp") String cgp,
            @Param("unit") String unitBiz,
            @Param("keyword") String keyword,
            @Param("correctYn") String correctYn,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow
    );

    int countAllPointRank(
            @Param("cgp") String cgp,
            @Param("unit") String unitBiz,
            @Param("keyword") String keyword
    );


    List<RankResponse> findTopPointRank(
            HashMap<String, Object> map
    );


    HashMap<String, Object> findUserInfoByEno(String eno);

    HashMap<String, Object> findHeaderInfoByEno(String eno);

}
