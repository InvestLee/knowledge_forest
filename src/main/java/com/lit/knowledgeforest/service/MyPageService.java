package com.lit.knowledgeforest.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lit.knowledgeforest.dto.RankResponse;
import com.lit.knowledgeforest.dto.RecentSolvedResponse;
import com.lit.knowledgeforest.dto.SolvedStatisticResponse;
import com.lit.knowledgeforest.entity.HistoryEntity;
import com.lit.knowledgeforest.repository.HistoryRepository;
import com.lit.knowledgeforest.repository.PointsRepository;
import com.lit.knowledgeforest.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final HistoryRepository historyRepository;

    private final PointsRepository pointsRepository;

    //전체 순위
    @Transactional
    public List<RankResponse> getPointsRank(){
        List<Object[]> pointRankList = pointsRepository.findAllPointRank();
        List<RankResponse> rankResponseList = new ArrayList<>();
        for(Object[] pointRank : pointRankList) {
            rankResponseList.add(
                    new RankResponse(
                            new BigDecimal(pointRank[0].toString()),
                            pointRank[1].toString(),
                            new BigDecimal(pointRank[2].toString())
                    )
            );
        }
        return rankResponseList;
    }

    //본인 순위
    @Transactional
    public RankResponse getMyPointsRank(String eno){
        List<Object[]> pointRank = pointsRepository.findMyRank(eno);

        return new RankResponse(
                new BigDecimal(pointRank.get(0)[0].toString()),
                pointRank.get(0)[1].toString(),
                new BigDecimal(pointRank.get(0)[2].toString())
        );
    }

    //본인이 최근 해결한 문제
    @Transactional
    public List<RecentSolvedResponse> getRecentSolved(String eno){
        List<HistoryEntity> historyEntityList = historyRepository.findAllByPkEnoAndCorrectYnOrderByLtChDttiDesc(eno,"Y");
        List<RecentSolvedResponse> mySolvecHistory = new ArrayList<>();
        for(HistoryEntity historyEntity : historyEntityList) {
            mySolvecHistory.add(
                    new RecentSolvedResponse(
                            historyEntity.getPk().getEno(),
                            historyEntity.getPk().getQuestionNo(),
                            historyEntity.getCorrectYn(),
                            historyEntity.getFstCrtUsid(),
                            historyEntity.getFstCrtDtti(),
                            historyEntity.getLtChUsid(),
                            historyEntity.getLtChDtti()
                    )
            );
        }
        return mySolvecHistory;
    }

    //해결 문제 통계
    @Transactional
    public List<SolvedStatisticResponse> getSolvedStatistic(String eno){
        List<Object[]> statisticList = historyRepository.findAllMySolvedStatistic(eno, "Y");
        List<SolvedStatisticResponse> myStatistic = new ArrayList<>();
        for(Object[] statistic : statisticList) {
            myStatistic.add(
                    new SolvedStatisticResponse(
                            statistic[0].toString(),
                            new BigDecimal(statistic[1].toString()),
                            new BigDecimal(statistic[2].toString())
                    )
            );
        }
        return myStatistic;
    }
}
