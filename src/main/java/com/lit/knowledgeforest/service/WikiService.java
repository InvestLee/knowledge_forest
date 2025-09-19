package com.lit.knowledgeforest.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lit.knowledgeforest.dto.QuestionList;
import com.lit.knowledgeforest.dto.QuizChoice;
import com.lit.knowledgeforest.code.Level;
import com.lit.knowledgeforest.code.QuestionType;
import com.lit.knowledgeforest.code.Status;
import com.lit.knowledgeforest.dto.NewQuiz;
import com.lit.knowledgeforest.dto.Question;
import com.lit.knowledgeforest.entity.QuestionChoiceEntity;
import com.lit.knowledgeforest.entity.QuestionEntity;
import com.lit.knowledgeforest.entity.primarykey.QuestionChoicePK;
import com.lit.knowledgeforest.repository.QuestionChoiceRepository;
import com.lit.knowledgeforest.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class WikiService {

    private final QuestionRepository questionRepository;
    private final QuestionChoiceRepository questionChoiceRepository;

    //문제 생성
    @Transactional
    public void createQuestion(NewQuiz q, String userId) {
        BigDecimal questionNo = questionRepository.getNextQuestionNo();
        System.out.print("n : ");
        System.out.println(questionNo);

        if(q.getTypeCd().equals("3")) {
            for(QuizChoice questionChoice : q.getChoices()) {
                System.out.println(questionNo);
                System.out.println(questionChoice.getChoiceNo());
                System.out.println(questionChoice.getContent());
                questionChoiceRepository.save(
                        new QuestionChoiceEntity(
                                new QuestionChoicePK(
                                        questionNo,
                                        questionChoice.getChoiceNo()
                                ),
                                questionChoice.getContent()
                        )
                );
            }
        }

        questionRepository.save(
                converToEntity(q, questionNo, Status.PENDING, userId)
        );
    }

    //문제 수정
    @Transactional
    public void upateQuestion(Question q, String userId) {

        BigDecimal questionNo = q.getQuestionNo();

        QuestionEntity qe = questionRepository.findById(questionNo)
                .orElseThrow(() -> new RuntimeException("문제 찾지 못함"));

        if (!qe.getFstCrtUsid().equals(userId)) {
            System.out.println("본인 문제만 수정 가능");
            return;
        }

        if (qe.getStatus().equals(Status.APPROVE)) {
            System.out.println("기승인된 문제 수정 불가능");
        }

        //객관식 문항 수정
        if(q.getTypeCd().equals("3")) {
            questionChoiceRepository.deleteByPkQuestionNo(questionNo);
            for(QuizChoice questionChoice : q.getChoices()) {
                questionChoiceRepository.save(
                        new QuestionChoiceEntity(
                                new QuestionChoicePK(
                                        questionNo,
                                        questionChoice.getChoiceNo()
                                ),
                                questionChoice.getContent()
                        )
                );
            }
        }

        questionRepository.save(updateEntity(qe, q, userId));
    }

    // 문제 삭제
    @Transactional
    public void deleteQuestion(BigDecimal questionNo) {
        questionRepository.deleteById(questionNo);
    }

    // 승인된 문제 리스트 (카테고리별)
    public Page<QuestionEntity> getApprovedQuestionsByCategory(Pageable pageable, String categoryName) {
        return questionRepository.findByStatusAndCategoryName(Status.APPROVE.status(), categoryName, pageable);
    }

    // 승인된 문제 리스트 (카테고리별) 검색
    public Page<QuestionEntity> getApprovedQuestionsByCategory(Pageable pageable, String categoryName, String search) {
        return questionRepository.findByStatusAndCategoryNameAndContentContaining(Status.APPROVE.status(), categoryName, search, pageable);
    }

    // 승인된 문제 리스트 (전체)
    public Page<QuestionEntity> getApprovedQuestions(Pageable pageable){
        return questionRepository.findByStatus(Status.APPROVE.status(), pageable);
    }

    // 승인된 문제 리스트 (전체) 검색
    public Page<QuestionEntity> getApprovedQuestions(Pageable pageable, String search){
        return questionRepository.findByStatusAndContentContaining(Status.APPROVE.status(), search, pageable);
    }

    // 문제 상세 보기
    public Question getQuestionDetails(BigDecimal questionNo) {
        QuestionEntity q = questionRepository.findById(questionNo)
                .orElseThrow(() -> new RuntimeException("문제 찾지 못함"));


        List<QuestionChoiceEntity> qc = new ArrayList<>();
        if(q.getTypeCd().equals("3")) {
            qc = questionChoiceRepository.findByPkQuestionNoOrderByPkChoiceNo(q.getQuestionNo());
        }

        return converToDto(q, qc);
    }

    public QuestionEntity converToEntity(
            NewQuiz qr,
            BigDecimal questionNo,
            Status status,
            String userId
    ) {
        return QuestionEntity.builder()
                .questionNo(questionNo)
                .content(qr.getContent())
                .description(qr.getDescription())
                .answer(qr.getAnswer())
                .typeCd(qr.getTypeCd())
                .levelCd(qr.getLevelCd())
                .categoryName(qr.getCategoryName())
                .status(status.status())
                .points(qr.getPoints())
                .fstCrtUsid(userId)
                .fstCrtDtti(LocalDateTime.now())
                .ltChUsid(userId)
                .ltChDtti(LocalDateTime.now())
                .build();
    }


    // DTO -> Entity 변환
    public QuestionEntity converToEntity(
            Question qr,
            Status status,
            String userId
    ) {
        return QuestionEntity.builder()
                .content(qr.getContent())
                .description(qr.getDescription())
                .answer(qr.getAnswer())
                .typeCd(qr.getTypeCd())
                .levelCd(qr.getLevelCd())
                .categoryName(qr.getCategoryName())
                .status(status.status())
                .points(qr.getPoints())
                .rejectCn(qr.getRejectCn())
                .fstCrtUsid(userId)
                .fstCrtDtti(LocalDateTime.now())
                .ltChUsid(userId)
                .ltChDtti(LocalDateTime.now())
                .build();
    }


    // Entity -> DTO 변환
    public Question converToDto(QuestionEntity q, List<QuestionChoiceEntity> qc) {
        Question qr = new Question();

        qr.setQuestionNo(q.getQuestionNo());
        qr.setContent(q.getContent());
        qr.setDescription(q.getDescription());
        qr.setAnswer(q.getAnswer());
        qr.setTypeCd(q.getTypeCd());
        qr.setTypeName(QuestionType.getNameByCode(q.getTypeCd()));
        qr.setLevelCd(q.getLevelCd());
        qr.setLevelName(Level.getNameByCode(q.getLevelCd()));
        qr.setCategoryName(q.getCategoryName());
        qr.setPoints(q.getPoints());
        qr.setRejectCn(q.getRejectCn());
        qr.setFstCrtUsid(q.getFstCrtUsid());
        qr.setFstCrtDtti(q.getFstCrtDtti());
        if(!qc.isEmpty()) {
            qr.setChoices(qc.stream()
                    .map(o -> new QuizChoice(o))
                    .collect(Collectors.toList()));
        }
        return qr;
    }

    public QuestionEntity updateEntity(QuestionEntity target, Question input, String userId) {
        return QuestionEntity.builder()
                .questionNo(target.getQuestionNo())
                .content(input.getContent())
                .description(input.getDescription())
                .answer(input.getAnswer())
                .typeCd(input.getTypeCd())
                .levelCd(input.getLevelCd())
                .status(target.getStatus())
                .points(input.getPoints())
                .categoryName(input.getCategoryName())
                .fstCrtUsid(target.getFstCrtUsid())
                .fstCrtDtti(target.getFstCrtDtti())
                .ltChUsid(userId)
                .ltChDtti(LocalDateTime.now())
                .build();
    }
}
