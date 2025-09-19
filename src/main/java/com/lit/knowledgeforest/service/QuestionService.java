package com.lit.knowledgeforest.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lit.knowledgeforest.dto.Code;
import com.lit.knowledgeforest.dto.Question;
import com.lit.knowledgeforest.dto.QuestionList;
import com.lit.knowledgeforest.dto.Quiz;
import com.lit.knowledgeforest.dto.QuizChoice;
import com.lit.knowledgeforest.entity.CodeEntity;
import com.lit.knowledgeforest.entity.HistoryEntity;
import com.lit.knowledgeforest.entity.QuestionChoiceEntity;
import com.lit.knowledgeforest.entity.QuestionEntity;
import com.lit.knowledgeforest.entity.UserInfoEntity;
import com.lit.knowledgeforest.entity.primarykey.HistoryPK;
import com.lit.knowledgeforest.repository.CodeRepository;
import com.lit.knowledgeforest.repository.QuestionRepository;
import com.lit.knowledgeforest.repository.UserInfoRepository;
import com.lit.knowledgeforest.repository.HistoryRepository;
import com.lit.knowledgeforest.repository.QuestionChoiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final CodeRepository codeRepository;
    private final HistoryRepository historyRepository;
    private final UserInfoRepository userInfoRepository;
    private final QuestionChoiceRepository questionChoiceRepository;

    // 문제 카테고리 검색
    public List<Code> getAllQuizCategory(){

        List<CodeEntity> categories = codeRepository.findByPkIcC("QZ_CTG");

        return categories.stream()
                .map(o -> new Code(o))
                .collect(Collectors.toList());
    }

    // 문제 레벨 검색
    public List<Code> getAllQuizLevel(){

        List<CodeEntity> levels = codeRepository.findByPkIcC("QZ_LEVEL");

        return levels.stream()
                .map(o -> new Code(o))
                .collect(Collectors.toList());
    }

    // 문제 유형 검색
    public List<Code> getAllQuizType(){

        List<CodeEntity> types = codeRepository.findByPkIcC("QZ_TYPE");

        return types.stream()
                .map(o -> new Code(o))
                .collect(Collectors.toList());
    }

    // 그 외 검색
    public List<Code> getAllType(String icC){

        List<CodeEntity> types = codeRepository.findByPkIcC(icC);

        return types.stream()
                .map(o -> new Code(o))
                .collect(Collectors.toList());
    }

    // 문제 카테고리 값 도출
    public Code getValQuizCategory(String icVlcn){

        CodeEntity category = codeRepository.findByPkIcVlAndIcVlcn("QZ_CTG", icVlcn);

        return Code.builder()
                .icVl(category.getPk().getIcVl())
                .icVlcn(icVlcn)
                .build();
    }

    // 문제 레벨 값 도출
    public Code getValQuizLevel(String icVlcn){

        CodeEntity level  = codeRepository.findByPkIcVlAndIcVlcn("QZ_LEVEL", icVlcn);

        return Code.builder()
                .icVl(level.getPk().getIcVl())
                .icVlcn(icVlcn)
                .build();
    }

    // 문제 유형 값 도출
    public Code getAllQuizType(String icVlcn){

        CodeEntity type = codeRepository.findByPkIcVlAndIcVlcn("QZ_TYPE", icVlcn);

        return Code.builder()
                .icVl(type.getPk().getIcVl())
                .icVlcn(icVlcn)
                .build();
    }

    // 카테고리, 레벨 별 문제 리스트 조회
    public List<Quiz> getCategoryQuestion(String questionId, String levelId, String eno){

        List<QuestionEntity> questions = questionRepository.findByQuestionIdAndLevelId(questionId, levelId, eno);

        return questions.stream()
                .map(o -> new Quiz(o))
                .collect(Collectors.toList());
    }

    // 문제풀이 결과 update
    public void solveQuiz(String eno, BigDecimal questionNo, BigDecimal points, boolean isCorrect) {
        String correctYn = "N";

        if(isCorrect) {
            correctYn = "Y";
        }

        HistoryEntity history = new HistoryEntity(new HistoryPK(eno, questionNo), correctYn);

        historyRepository.save(history);

        if(isCorrect) {
            userInfoRepository.updatePoints(eno, points);
        }

    }

    //객관시 문항
    public List<QuizChoice> getChoiceQuestion(BigDecimal questionNo){
        return questionChoiceRepository.findByPkQuestionNoOrderByPkChoiceNo(questionNo)
                .stream()
                .map(o -> new QuizChoice(o))
                .collect(Collectors.toList());
    }
}
