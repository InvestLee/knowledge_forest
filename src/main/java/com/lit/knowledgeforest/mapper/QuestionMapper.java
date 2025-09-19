package com.lit.knowledgeforest.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.lit.knowledgeforest.dto.RecentSolvedResponse;

@Repository
@Mapper
public interface QuestionMapper {

    RecentSolvedResponse getTodayQuizByEno(HashMap<String, Object> map);

    int countUnsolvedQuestions(String eno);
}

