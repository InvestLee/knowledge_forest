package com.lit.knowledgeforest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lit.knowledgeforest.dto.RankResponse;
import com.lit.knowledgeforest.dto.RecentSolvedResponse;
import com.lit.knowledgeforest.dto.SolvedStatisticResponse;
import com.lit.knowledgeforest.entity.CategoryEntity;
import com.lit.knowledgeforest.entity.HistoryEntity;
import com.lit.knowledgeforest.entity.PointsEntity;
import com.lit.knowledgeforest.entity.QuestionEntity;
import com.lit.knowledgeforest.entity.primarykey.CategoryPK;
import com.lit.knowledgeforest.entity.primarykey.HistoryPK;
import com.lit.knowledgeforest.repository.CategoryRepository;
import com.lit.knowledgeforest.repository.HistoryRepository;
import com.lit.knowledgeforest.repository.PointsRepository;
import com.lit.knowledgeforest.repository.QuestionRepository;
import com.lit.knowledgeforest.service.MyPageService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MyPageTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private PointsRepository pointsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MyPageService myPageService;

    @Test
    void 풀이_이력_등록() throws Exception{
        historyRepository.save(
                HistoryEntity.builder()
                        .pk(HistoryPK.builder()
                                .eno("LD10630")
                                .questionNo(new BigDecimal("10630"))
                                .build()
                        )
                        .correctYn("Y")
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        Optional<HistoryEntity> testHistoryEntity = historyRepository.findByPkEnoAndPkQuestionNo("LD10630", 10630L);
        assertTrue(testHistoryEntity.isPresent());
    }

    @Test
    void 순위_조회() throws Exception{

        pointsRepository.save(
                PointsEntity.builder()
                        .eno("LD90630")
                        .points(new BigDecimal("50"))
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        pointsRepository.save(
                PointsEntity.builder()
                        .eno("LD90631")
                        .points(new BigDecimal("30"))
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        pointsRepository.save(
                PointsEntity.builder()
                        .eno("LD90632")
                        .points(new BigDecimal("20"))
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        pointsRepository.save(
                PointsEntity.builder()
                        .eno("LD90633")
                        .points(new BigDecimal("30"))
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        List<RankResponse> rankList = myPageService.getPointsRank();

        assertEquals(rankList.size(), 4);

        assertEquals(rankList.get(0).getEno(), "LD90630");
        assertEquals(rankList.get(0).getRank(), new BigDecimal("1"));

        assertEquals(rankList.get(1).getEno(), "LD90631");
        assertEquals(rankList.get(1).getRank(), new BigDecimal("2"));

        assertEquals(rankList.get(2).getEno(), "LD90633");
        assertEquals(rankList.get(2).getRank(), new BigDecimal("2"));

        assertEquals(rankList.get(3).getEno(), "LD90632");
        assertEquals(rankList.get(3).getRank(), new BigDecimal("4"));
    }

    @Test
    void 순위_조회_본인() throws Exception{

        pointsRepository.save(
                PointsEntity.builder()
                        .eno("LD90630")
                        .points(new BigDecimal("50"))
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        pointsRepository.save(
                PointsEntity.builder()
                        .eno("LD90631")
                        .points(new BigDecimal("30"))
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        pointsRepository.save(
                PointsEntity.builder()
                        .eno("LD90632")
                        .points(new BigDecimal("20"))
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        pointsRepository.save(
                PointsEntity.builder()
                        .eno("LD90633")
                        .points(new BigDecimal("30"))
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        RankResponse rank = myPageService.getMyPointsRank("LD90631");

        assertEquals(rank.getEno(), "LD90631");
        assertEquals(rank.getRank(), new BigDecimal("2"));
    }

    @Test
    void 문제_해결_이력() throws Exception{
        historyRepository.save(
                HistoryEntity.builder()
                        .pk(HistoryPK.builder()
                                .eno("LD10630")
                                .questionNo(new BigDecimal("10630"))
                                .build()
                        )
                        .correctYn("Y")
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        historyRepository.save(
                HistoryEntity.builder()
                        .pk(HistoryPK.builder()
                                .eno("LD10630")
                                .questionNo(new BigDecimal("10631"))
                                .build()
                        )
                        .correctYn("N")
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now().plusHours(2L))
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now().plusHours(2L))
                        .build()
        );

        historyRepository.save(
                HistoryEntity.builder()
                        .pk(HistoryPK.builder()
                                .eno("LD10630")
                                .questionNo(new BigDecimal("10632"))
                                .build()
                        )
                        .correctYn("Y")
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now().plusHours(3L))
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now().plusHours(3L))
                        .build()
        );

        historyRepository.save(
                HistoryEntity.builder()
                        .pk(HistoryPK.builder()
                                .eno("LD10630")
                                .questionNo(new BigDecimal("10633"))
                                .build()
                        )
                        .correctYn("Y")
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now().plusHours(1L))
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now().plusHours(1L))
                        .build()
        );

        List<RecentSolvedResponse> SolvedList = myPageService.getRecentSolved("LD10630");

        assertEquals(SolvedList.size(), 3);

        assertEquals(SolvedList.get(0).getQuestionNo(), new BigDecimal("10632"));

        assertEquals(SolvedList.get(1).getQuestionNo(), new BigDecimal("10633"));

        assertEquals(SolvedList.get(2).getQuestionNo(), new BigDecimal("10630"));
    }

    @Test
    void 본인_통계() throws Exception{
        historyRepository.save(
                HistoryEntity.builder()
                        .pk(HistoryPK.builder()
                                .eno("LD10630")
                                .questionNo(new BigDecimal("10630"))
                                .build()
                        )
                        .correctYn("Y")
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        historyRepository.save(
                HistoryEntity.builder()
                        .pk(HistoryPK.builder()
                                .eno("LD10630")
                                .questionNo(new BigDecimal("10631"))
                                .build()
                        )
                        .correctYn("N")
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now().plusHours(2L))
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now().plusHours(2L))
                        .build()
        );

        historyRepository.save(
                HistoryEntity.builder()
                        .pk(HistoryPK.builder()
                                .eno("LD10630")
                                .questionNo(new BigDecimal("10632"))
                                .build()
                        )
                        .correctYn("Y")
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now().plusHours(3L))
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now().plusHours(3L))
                        .build()
        );

        historyRepository.save(
                HistoryEntity.builder()
                        .pk(HistoryPK.builder()
                                .eno("LD10630")
                                .questionNo(new BigDecimal("10633"))
                                .build()
                        )
                        .correctYn("Y")
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now().plusHours(1L))
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now().plusHours(1L))
                        .build()
        );

        categoryRepository.save(
                CategoryEntity.builder()
                        .pk(CategoryPK.builder()
                                .questionNo(new BigDecimal("10630"))
                                .categoryName("IT운영팀")
                                .build()
                        )
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        categoryRepository.save(
                CategoryEntity.builder()
                        .pk(CategoryPK.builder()
                                .questionNo(new BigDecimal("10631"))
                                .categoryName("네트워크")
                                .build()
                        )
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        categoryRepository.save(
                CategoryEntity.builder()
                        .pk(CategoryPK.builder()
                                .questionNo(new BigDecimal("10632"))
                                .categoryName("데이터베이스")
                                .build()
                        )
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        categoryRepository.save(
                CategoryEntity.builder()
                        .pk(CategoryPK.builder()
                                .questionNo(new BigDecimal("10633"))
                                .categoryName("IT운영팀")
                                .build()
                        )
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        questionRepository.save(
                QuestionEntity.builder()
                        .questionNo(new BigDecimal("10630"))
                        .content("문제1")
                        .description("해설1")
                        .answer("정답1")
                        .typeCd("1")
                        .levelCd("1")
                        .status("Y")
                        .points(new BigDecimal("10"))
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        questionRepository.save(
                QuestionEntity.builder()
                        .questionNo(new BigDecimal("10631"))
                        .content("문제2")
                        .description("해설2")
                        .answer("정답2")
                        .typeCd("2")
                        .levelCd("2")
                        .status("Y")
                        .points(new BigDecimal("20"))
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        questionRepository.save(
                QuestionEntity.builder()
                        .questionNo(new BigDecimal("10632"))
                        .content("문제3")
                        .description("해설3")
                        .answer("정답3")
                        .typeCd("3")
                        .levelCd("1")
                        .status("Y")
                        .points(new BigDecimal("30"))
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        questionRepository.save(
                QuestionEntity.builder()
                        .questionNo(new BigDecimal("10633"))
                        .content("문제4")
                        .description("해설4")
                        .answer("정답1")
                        .typeCd("3")
                        .levelCd("2")
                        .status("Y")
                        .points(new BigDecimal("50"))
                        .fstCrtUsid("LD10630")
                        .fstCrtDtti(LocalDateTime.now())
                        .ltChUsid("LD10630")
                        .ltChDtti(LocalDateTime.now())
                        .build()
        );

        List<SolvedStatisticResponse> statisticList = myPageService.getSolvedStatistic("LD10630");

        assertEquals(statisticList.size(), 2);

        assertEquals(statisticList.get(0).getCategoryName(), "IT운영팀");
        assertEquals(statisticList.get(0).getSolvedCount(), new BigDecimal("2"));
        assertEquals(statisticList.get(0).getTotalPoint(), new BigDecimal("60"));

        assertEquals(statisticList.get(1).getCategoryName(), "데이터베이스");
        assertEquals(statisticList.get(1).getSolvedCount(), new BigDecimal("1"));
        assertEquals(statisticList.get(1).getTotalPoint(), new BigDecimal("30"));
    }
}
