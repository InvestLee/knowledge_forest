package com.lit.knowledgeforest.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lit.knowledgeforest.code.Level;
import com.lit.knowledgeforest.code.QuestionType;
import com.lit.knowledgeforest.config.MyBatisConfigChecker;
import com.lit.knowledgeforest.dto.PointHistResponse;
import com.lit.knowledgeforest.dto.RankRequest;
import com.lit.knowledgeforest.dto.RankResponse;
import com.lit.knowledgeforest.dto.RecentSolvedRequest;
import com.lit.knowledgeforest.dto.RecentSolvedResponse;
import com.lit.knowledgeforest.dto.SolvedStatisticResponse;
import com.lit.knowledgeforest.dto.StreakResponse;
import com.lit.knowledgeforest.dto.UserResponse;
import com.lit.knowledgeforest.entity.PointHistoryEntity;
import com.lit.knowledgeforest.entity.UserEntity;
import com.lit.knowledgeforest.mapper.HistoryMapper;
import com.lit.knowledgeforest.mapper.UserInfoMapper;
import com.lit.knowledgeforest.repository.HistoryRepository;
import com.lit.knowledgeforest.repository.PointHistoryRepository;
import com.lit.knowledgeforest.repository.UserInfoRepository;
import com.lit.knowledgeforest.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final HistoryRepository historyRepository;

    private final UserInfoRepository userInfoRepository;

    private final UserRepository userRepository;

    private final UserInfoMapper userInfoMapper;

    private final HistoryMapper historyMapper;

    private final PointHistoryRepository pointHistoryRepository;

    private final MyBatisConfigChecker myBatisConfigChecker;

    //전체 순위
    @Transactional
    public Page<RankResponse> getPointsRank(Pageable pageable){
        int startRow = pageable.getPageNumber() * pageable.getPageSize() + 1;
        int endRow = startRow + pageable.getPageSize() - 1;

        List<RankResponse> response = userInfoMapper.findAllPointRank(
                "",
                "",
                "",
                "Y",
                startRow,
                endRow
        );

        Long totalCnt = userRepository.count();
        return new PageImpl<>(response, pageable, totalCnt);
    }

    //전체 순위 검색
    @Transactional
    public Page<RankResponse> getPointsRankSearch(RankRequest request){
        int startRow = request.getPage() * request.getSize() + 1;
        int endRow = (request.getPage() + 1) * request.getSize();

        List<RankResponse> response = userInfoMapper.findAllPointRank(
                request.getCgp(),
                request.getUnit(),
                request.getKeyword(),
                "Y",
                startRow,
                endRow
        );

        int totalCnt = userInfoMapper.countAllPointRank(
                request.getCgp(),
                request.getUnit(),
                request.getKeyword()
        );

        return new PageImpl<>(
                response,
                PageRequest.of(
                        request.getPage(),
                        request.getSize()
                ),
                totalCnt
        );
    }

    //본인 순위
    @Transactional
    public RankResponse getMyPointsRank(String eno){
        List<Object[]> pointRank = userInfoRepository.findMyRank(eno, "Y");
        UserEntity user = userRepository.findByPkEno(eno)
                .orElseThrow(() -> new RuntimeException("회원정보 없음"));
        return new RankResponse(
                new BigDecimal(pointRank.get(0)[0].toString()),
                pointRank.get(0)[1].toString(),
                user.getEmpNm(),
                new BigDecimal(pointRank.get(0)[2].toString()),
                new BigDecimal(pointRank.get(0)[3].toString())
        );
    }

    //본인이 최근 해결한 문제
    @Transactional
    public Page<RecentSolvedResponse> getRecentSolved(String eno, Pageable pageable){
        int startRow = pageable.getPageNumber() * pageable.getPageSize() + 1;
        int endRow = startRow + pageable.getPageSize() - 1;

        List<Object[]> recentSolvedList = historyRepository.findAllByPkEnoAndCorrectYnOrderByLtChDttiDesc(
                eno,
                "Y",
                startRow,
                endRow
        );
        int totalCnt = historyRepository.countByPkEnoAndCorrectYn(eno, "Y");

        List<RecentSolvedResponse> response = new ArrayList<>();
        for(Object[] recentSolved : recentSolvedList) {
            response.add(
                    new RecentSolvedResponse(
                            recentSolved[0].toString(),
                            new BigDecimal(recentSolved[1].toString()),
                            recentSolved[2].toString(),
                            recentSolved[3].toString().length() < 20 ? recentSolved[3].toString() : recentSolved[3].toString() + "...",
                            QuestionType.getNameByCode(recentSolved[4].toString()),
                            Level.getNameByCode(recentSolved[5].toString()),
                            recentSolved[6].toString(),
                            new BigDecimal(recentSolved[7].toString()),
                            recentSolved[8].toString(),
                            recentSolved[9].toString(),
                            recentSolved[10].toString(),
                            recentSolved[11].toString()
                    )
            );
        }

        return new PageImpl<>(response, pageable, totalCnt);
    }

    //본인이 최근 해결한 문제
    @Transactional
    public List<RecentSolvedResponse> getRecentFiveSolved(String eno){
        List<Object[]> recentSolvedList = historyRepository.findAllByPkEnoAndCorrectYnOrderByLtChDttiDesc(
                eno,
                "Y",
                1,
                5
        );

        List<RecentSolvedResponse> response = new ArrayList<>();
        for(Object[] recentSolved : recentSolvedList) {
            response.add(
                    new RecentSolvedResponse(
                            recentSolved[0].toString(),
                            new BigDecimal(recentSolved[1].toString()),
                            recentSolved[2].toString(),
                            recentSolved[3].toString().length() < 20 ? recentSolved[3].toString() : recentSolved[3].toString() + "...",
                            QuestionType.getNameByCode(recentSolved[4].toString()),
                            Level.getNameByCode(recentSolved[5].toString()),
                            recentSolved[6].toString(),
                            new BigDecimal(recentSolved[7].toString()),
                            recentSolved[8].toString(),
                            recentSolved[9].toString(),
                            recentSolved[10].toString(),
                            recentSolved[11].toString()
                    )
            );
        }
        return response;
    }

    //본인 해결 문제 검색
    @Transactional
    public Page<RecentSolvedResponse> findSolvedQuestions(String eno, RecentSolvedRequest request){
        int startRow = request.getPage() * request.getSize() + 1;
        int endRow = (request.getPage() + 1) * request.getSize();

        List<RecentSolvedResponse> response = historyMapper.findSolvedQuestions(
                eno,
                "Y",
                request.getCategory(),
                request.getLev(),
                request.getType(),
                request.getKeyword(),
                startRow,
                endRow
        );
        response.forEach(r -> r.toEnum(
                r.getLev(),
                r.getType()
        ));

        int totalCnt = historyMapper.countSolvedQuestions(
                eno,
                "Y",
                request.getCategory(),
                request.getLev(),
                request.getType(),
                request.getKeyword()
        );

        return new PageImpl<>(
                response,
                PageRequest.of(request.getPage(), request.getSize()),
                totalCnt
        );
    }

    //해결 문제 통계
    @Transactional
    public List<SolvedStatisticResponse> getSolvedStatistic(String eno){

        List<Object[]> statisticList = historyRepository.findAllMySolvedStatistic(
                eno,
                "Y"
        );

        List<SolvedStatisticResponse> response = new ArrayList<>();
        for(Object[] statistic : statisticList) {
            response.add(new SolvedStatisticResponse(
                    statistic[0].toString(),
                    new BigDecimal(statistic[1].toString()),
                    new BigDecimal(statistic[2].toString())
            ));
        }

        return response;
    }

    //스트릭 조회
    @Transactional
    public List<StreakResponse> getStreakYear(String eno){

        List<Object[]> streakList = historyRepository.findStreakYear(
                eno,
                "Y"
        );

        List<StreakResponse> response = new ArrayList<>();
        for(Object[] streak : streakList) {
            response.add(new StreakResponse(
                    streak[0].toString(),
                    streak[1].toString(),
                    new BigDecimal(streak[2].toString())
            ));
        }

        return response;
    }

    //본인 정보 조회
    @Transactional
    public UserResponse findUser(String eno) {
        UserEntity userEntity = userRepository.findByPkEno(eno)
                .orElseThrow(() -> new RuntimeException("회원정보 없음"));
        return UserResponse.builder()
                .eno(userEntity.getPk().getEno())
                .vlYn(userEntity.getPk().getVlYn())
                .ldEmpC(userEntity.getLdEmpC())
                .bd(userEntity.getBd())
                .jgdNm(userEntity.getJgdNm())
                .ptNm(userEntity.getPtNm())
                .pwd(userEntity.getPwd())
                .empNm(userEntity.getEmpNm())
                .unitBiz(userEntity.getUnitBiz())
                .acno(userEntity.getAcno())
                .bnkNm(userEntity.getBnkNm())
                .subDtStYm(userEntity.getSubDtStYm())
                .miDtStYm(userEntity.getMiDtStYm())
                .isAdmin(userEntity.getIsAdmin())
                .cgp(userEntity.getCgp())
                .vald(userEntity.getVald())
                .hMiDtStYm(userEntity.getHMiDtStYm())
                .hSubDtStYm(userEntity.getHSubDtStYm())
                .grpCd(userEntity.getGrpCd())
                .bdLcYn(userEntity.getBdLcYn())
                .ipYn(userEntity.getIpYn())
                .cashbeeNo(userEntity.getCashbeeNo())
                .elcAdd(userEntity.getElcAdd())
                .lateType(userEntity.getLateType())
                .build();
    }

    public Page<PointHistoryEntity> findPointHistory(Pageable pageable, String eno) {
        return pointHistoryRepository.findByEno(eno, pageable);
    }

    //포인트 적립 이력
    @Transactional
    public Page<PointHistResponse> getPointHist(String eno, Pageable pageable){
        int startRow = pageable.getPageNumber() * pageable.getPageSize() + 1;
        int endRow = startRow + pageable.getPageSize() - 1;

        List<Object[]> pointHistList = pointHistoryRepository.findAllByEnoOrderByLtChDttiDesc(
                eno,
                startRow,
                endRow
        );

        int totalCnt = pointHistoryRepository.countByEno(eno);

        List<PointHistResponse> response = new ArrayList<>();
        for(Object[] pointHist : pointHistList) {
            response.add(
                    new PointHistResponse(
                            pointHist[0].toString(),
                            new BigDecimal(pointHist[1].toString()),
                            pointHist[2].toString(),
                            pointHist[3].toString(),
                            pointHist[4].toString(),
                            pointHist[5].toString(),
                            pointHist[6].toString()
                    )
            );
        }

        return new PageImpl<>(response, pageable, totalCnt);
    }
    //최근 포인트 적립 이력
    @Transactional
    public List<PointHistResponse> getRecentFivePointHist(String eno){
        List<Object[]> pointHistList = pointHistoryRepository.findAllByEnoOrderByLtChDttiDesc(
                eno,
                1,
                5
        );

        List<PointHistResponse> response = new ArrayList<>();
        for(Object[] pointHist : pointHistList) {
            response.add(
                    new PointHistResponse(
                            pointHist[0].toString(),
                            new BigDecimal(pointHist[1].toString()),
                            pointHist[2].toString(),
                            pointHist[3].toString(),
                            pointHist[4].toString(),
                            pointHist[5].toString(),
                            pointHist[6].toString()
                    )
            );
        }
        return response;
    }

}
