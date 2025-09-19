package com.lit.knowledgeforest.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lit.knowledgeforest.code.Status;
import com.lit.knowledgeforest.dto.Question;
import com.lit.knowledgeforest.dto.QuizChoice;
import com.lit.knowledgeforest.entity.PointHistoryEntity;
import com.lit.knowledgeforest.entity.QuestionChoiceEntity;
import com.lit.knowledgeforest.entity.QuestionEntity;
import com.lit.knowledgeforest.entity.UserEntity;
import com.lit.knowledgeforest.entity.primarykey.QuestionChoicePK;
import com.lit.knowledgeforest.repository.PointHistoryRepository;
import com.lit.knowledgeforest.repository.QuestionChoiceRepository;
import com.lit.knowledgeforest.repository.QuestionRepository;
import com.lit.knowledgeforest.repository.UserInfoRepository;
import com.lit.knowledgeforest.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminPageService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final PointHistoryRepository pointHistRepository;
    private final QuestionChoiceRepository questionChoiceRepository;

    public boolean isAdmin(String eno) {
        UserEntity userEntity = userRepository.findByPkEno(eno)
                .orElseThrow(() -> new EntityNotFoundException("유저 정보가 없습니다."));

        boolean isAdminOrManager = (userEntity.getIsAdmin().equals("2") || userEntity.getIsAdmin().equals("3"));
        return isAdminOrManager;
    }

    public Page<QuestionEntity> findPending(Pageable pageable) {
        return questionRepository.findByStatus(Status.PENDING.status(), pageable);
    }

    public Page<QuestionEntity> findRejected(Pageable pageable) {
        return questionRepository.findByStatus(Status.REJECT.status(), pageable);
    }

    public Page<QuestionEntity> findApproved(Pageable pageable) {
        return questionRepository.findByStatus(Status.APPROVE.status(), pageable);
    }

    // 문제 승인
    @Transactional
    public void approveQuestion(Question question, String userId) {

        BigDecimal questionNo = question.getQuestionNo();

        QuestionEntity qe = questionRepository.findById(questionNo)
                .orElseThrow(() -> new RuntimeException("문제 찾지 못함"));

        System.out.println("1번 시작");

        //객관식 문항 수정
        if(question.getTypeCd().equals("3")) {
            questionChoiceRepository.deleteByPkQuestionNo(questionNo);
            for(QuizChoice questionChoice : question.getChoices()) {
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

        // 1) 문제 승인 상태로 변경
        questionRepository.save(
                approveEntity(qe, question, userId)
        );

        // 2) 문제 작성자에게 포인트 지급
        String writeEno = question.getFstCrtUsid();
        userInfoRepository.updatePoints(writeEno, question.getPoints());

        // 3) 포인트 지급 이력 저장
        String memo = "[문제 승인] " + question.getQuestionNo() + "번";
        pointHistRepository.save(
                insertHistory(question, memo, userId));

    }

    // 문제 반려
    @Transactional
    public void rejectQuestion(Question question, String userId) {

        BigDecimal questionNo = question.getQuestionNo();
        System.out.println("문제 반려");
        System.out.println(question);
        QuestionEntity qe = questionRepository.findById(questionNo)
                .orElseThrow(() -> new RuntimeException("문제 찾지 못함"));

        questionRepository.save(
                rejectEntity(qe, question, userId)
        );
    }

    // 문제 수정
    @Transactional
    public void updateQuestion(Question question, String userId) {

        BigDecimal questionNo = question.getQuestionNo();

        QuestionEntity qe = questionRepository.findById(questionNo)
                .orElseThrow(() -> new RuntimeException("문제 찾지 못함"));

        //객관식 문항 수정
        if(question.getTypeCd().equals("3")) {
            questionChoiceRepository.deleteByPkQuestionNo(questionNo);
            for(QuizChoice questionChoice : question.getChoices()) {
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

        // 1) 문제 승인 상태로 변경
        questionRepository.save(
                approveEntity(qe, question, userId)
        );
    }

    // 문제 삭제
    @Transactional
    public void removeQuestion(Question question) {

        BigDecimal questionNo = question.getQuestionNo();
        System.out.println("문제 삭제");
        System.out.println(question);
        QuestionEntity qe = questionRepository.findById(questionNo)
                .orElseThrow(() -> new RuntimeException("문제 찾지 못함"));

        questionRepository.delete(qe);
    }

    public QuestionEntity approveEntity(QuestionEntity target, Question input, String userId) {
        return QuestionEntity.builder()
                .questionNo(target.getQuestionNo())
                .content(input.getContent())
                .description(input.getDescription())
                .answer(input.getAnswer())
                .typeCd(input.getTypeCd())
                .levelCd(input.getLevelCd())
                .status(Status.APPROVE.status())
                .points(input.getPoints())
                .categoryName(input.getCategoryName())
                .fstCrtUsid(target.getFstCrtUsid())
                .fstCrtDtti(target.getFstCrtDtti())
                .ltChUsid(userId)
                .ltChDtti(LocalDateTime.now())
                .build();
    }

    public QuestionEntity rejectEntity(QuestionEntity target, Question input, String userId) {
        return QuestionEntity.builder()
                .questionNo(target.getQuestionNo())
                .content(target.getContent())
                .description(target.getDescription())
                .answer(target.getAnswer())
                .typeCd(target.getTypeCd())
                .levelCd(target.getLevelCd())
                .status(Status.REJECT.status())
                .points(target.getPoints())
                .categoryName(target.getCategoryName())
                .rejectCn(input.getRejectCn())
                .fstCrtUsid(target.getFstCrtUsid())
                .fstCrtDtti(target.getFstCrtDtti())
                .ltChUsid(userId)
                .ltChDtti(LocalDateTime.now())
                .build();
    }

    public QuestionEntity updateEntity(QuestionEntity target, Question input, String userId) {
        return QuestionEntity.builder()
                .questionNo(target.getQuestionNo())
                .content(input.getContent())
                .description(input.getDescription())
                .answer(input.getAnswer())
                .typeCd(input.getTypeCd())
                .levelCd(input.getLevelCd())
                .status(Status.APPROVE.status())
                .points(input.getPoints())
                .categoryName(input.getCategoryName())
                .fstCrtUsid(target.getFstCrtUsid())
                .fstCrtDtti(target.getFstCrtDtti())
                .ltChUsid(userId)
                .ltChDtti(LocalDateTime.now())
                .build();
    }


    public PointHistoryEntity insertHistory(Question question, String memo, String userId) {
        return PointHistoryEntity.builder()
                .eno(question.getFstCrtUsid())
                .points(question.getPoints())
                .memo(memo)
                .fstCrtUsid(userId)
                .fstCrtDtti(LocalDateTime.now())
                .ltChUsid(userId)
                .ltChDtti(LocalDateTime.now())
                .build();
    }
}
