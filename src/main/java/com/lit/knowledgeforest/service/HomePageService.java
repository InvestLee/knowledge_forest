package com.lit.knowledgeforest.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.lit.knowledgeforest.dto.RankResponse;
import com.lit.knowledgeforest.dto.RecentSolvedResponse;
import com.lit.knowledgeforest.entity.HistoryEntity;
import com.lit.knowledgeforest.entity.QuestionEntity;
import com.lit.knowledgeforest.entity.primarykey.HistoryPK;
import com.lit.knowledgeforest.mapper.HistoryMapper;
import com.lit.knowledgeforest.mapper.QuestionMapper;
import com.lit.knowledgeforest.mapper.UserInfoMapper;
import com.lit.knowledgeforest.repository.HistoryRepository;
import com.lit.knowledgeforest.repository.QuestionRepository;
import com.lit.knowledgeforest.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomePageService {

    private final UserInfoMapper userInfoMapper;
    private final QuestionMapper questionMapper;
    private final HistoryMapper historyMapper;
    private final HistoryRepository historyRepository;
    private final UserInfoRepository userInfoRepository;
    private final QuestionRepository questionRepository;
    // 상위 n개 랭킹
    public List<RankResponse> getFindTopPointRank(int num) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("correctYn", "Y");
        param.put("rank", num);
        return userInfoMapper.findTopPointRank(param);
    }

    // 오늘의 퀴즈 추출 (안 푼 문제 중에서 1개)
    public RecentSolvedResponse getTodayQuizByEno(String eno) {
        RecentSolvedResponse todayQuiz = null;

        // 오늘의 퀴즈를 안풀었을 경우 -> 오늘의 퀴즈 추출
        if (!isSolvedTodayQuiz(eno)) {
            int seed = getSeed(eno); // 랜덤 시드 고정

            // 미해결 문제가 없을 경우
            if (seed == -1) {
                return null;
            }

            HashMap<String, Object> param = new HashMap<>();
            param.put("eno", eno);
            param.put("limit", seed);

            todayQuiz = questionMapper.getTodayQuizByEno(param);
        }
        return todayQuiz;
    }

    // 오늘 푼 문제 있는지 여부
    private boolean isSolvedTodayQuiz(String eno) {
        HashMap<String, String> param = new HashMap<>();
        param.put("eno", eno);
        int res = historyMapper.getSolvedTodayQuizByEno(param);
        return res > 0;
    }

    // 문제풀이 결과 update
    public Map<String, Object> checkAnswer(String eno, BigDecimal questionNo, String userAnswer, BigDecimal points) {


        boolean isCorrect = isCorrectAnswer(questionNo, userAnswer);

        Map<String, Object> response = new HashMap<>();
        response.put("isCorrect", isCorrect);

        String correctYn = "N";

        if(isCorrect) {
            correctYn = "Y";
            BigDecimal point = new BigDecimal(points.intValue() * 2); //오늘의 퀴즈는 포인트 2배 이벤트
            userInfoRepository.updatePoints(eno, point);
            response.put("message", "🎉 정답입니다! +" + point + " 포인트가 적립되었습니다!");
        }else {
            response.put("message", "😢 아쉽게도 틀렸어요! 다시 위키에서 확인해보세요.");
        }

        HistoryEntity history = new HistoryEntity(new HistoryPK(eno, questionNo), correctYn);
        historyRepository.save(history);

        return response;
    }

    // 오늘 푼 문제 있는지 여부
    private boolean isCorrectAnswer(BigDecimal questionNo, String userAnswer) {
        QuestionEntity questionEntity = questionRepository.getQuestionInfoById(questionNo);
        return questionEntity.getAnswer().equalsIgnoreCase(userAnswer);
    }

    // 랜덤 시드 고정
    private int getSeed(String eno) {
        // 1. 시드 생성 (ENO + 날짜)
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // "20250730"
        String seedSource = eno + today;
        int seed = seedSource.hashCode();

        // 2. 미해결 문제 수 조회
        int totalCount = questionMapper.countUnsolvedQuestions(eno);
        if (totalCount == 0) {
            return -1; // 모든 문제를 다 푼 경우
        }

        // 3. 랜덤 offset 계산
        Random random = new Random(seed);
        int offset = random.nextInt(totalCount); // 0 ~ totalCount-1

        // 4. ROWNUM은 1부터 시작하므로 +1 반환
        return offset + 1;
    }

}

