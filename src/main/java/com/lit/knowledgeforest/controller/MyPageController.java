package com.lit.knowledgeforest.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lit.knowledgeforest.code.CategoryName;
import com.lit.knowledgeforest.code.Level;
import com.lit.knowledgeforest.code.QuestionType;
import com.lit.knowledgeforest.dto.Code;
import com.lit.knowledgeforest.dto.LoginResponse;
import com.lit.knowledgeforest.dto.PointHistResponse;
import com.lit.knowledgeforest.dto.RankRequest;
import com.lit.knowledgeforest.dto.RankResponse;
import com.lit.knowledgeforest.dto.RecentSolvedRequest;
import com.lit.knowledgeforest.dto.RecentSolvedResponse;
import com.lit.knowledgeforest.dto.SolvedStatisticResponse;
import com.lit.knowledgeforest.dto.StreakResponse;
import com.lit.knowledgeforest.dto.UserResponse;
import com.lit.knowledgeforest.entity.PointHistoryEntity;
import com.lit.knowledgeforest.service.MyPageService;
import com.lit.knowledgeforest.service.QuestionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final QuestionService questionService;

    @GetMapping("/mypage")
    public String initPage(Model model, HttpSession session) {
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String eno = loginResponse.getEno();

        UserResponse userResponse = myPageService.findUser(eno);
        RankResponse rankResponse = myPageService.getMyPointsRank(eno);
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("name", userResponse.getEmpNm());
        userMap.put("eno", userResponse.getEno());
        userMap.put("cgp", userResponse.getCgp());
        userMap.put("unitBiz", userResponse.getUnitBiz());
        userMap.put("point", rankResponse.getPoints().toString());
        userMap.put("rank", rankResponse.getRank().toString()+"위");
        model.addAttribute("user", userMap);
        //model.addAttribute("difficulty", );

        List<StreakResponse> Streacklist = myPageService.getStreakYear(eno);
        model.addAttribute("streaks", Streacklist);

        List<RecentSolvedResponse> recentSolved = myPageService.getRecentFiveSolved(eno);
        model.addAttribute("recentSolved", recentSolved);

        List<PointHistResponse> pointHist = myPageService.getRecentFivePointHist(eno);
        model.addAttribute("pointHist", pointHist);

        List<SolvedStatisticResponse> solvedStatistic = myPageService.getSolvedStatistic(eno);
        model.addAttribute("categoryStats", solvedStatistic);
        return "mypage";
    }

    @GetMapping("/mypage/solved")
    public String getSolvedListPage(@PageableDefault(size = 20, page = 0) Pageable pageable,
                                    Model model, HttpSession session
    ) {

        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String eno = loginResponse.getEno();

        Page<RecentSolvedResponse> recentSolved = myPageService.getRecentSolved(eno, pageable);

        model.addAttribute("recentSolved", recentSolved);

        model.addAttribute("categoryOptions", CategoryName.values());

        model.addAttribute("levelOptions", Level.values());

        model.addAttribute("typeOptions", QuestionType.values());

        return "solvedList";
    }

    @PostMapping("/mypage/solved")
    public ResponseEntity<Page<RecentSolvedResponse>> getSolvedQuestions(
            @RequestBody RecentSolvedRequest request, HttpSession session
    ) {
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String eno = loginResponse.getEno();
        return ResponseEntity.ok(myPageService.findSolvedQuestions(eno, request));
    }

    @GetMapping("/rank")
    public String getPointsRank(
            @PageableDefault(size = 20, page = 0) Pageable pageable,
            Model model
    ){
        Page<RankResponse> rankList = myPageService.getPointsRank(pageable);
        model.addAttribute("rankingBody", rankList);

        List<Code> cgpList = questionService.getAllType("CGP");
        model.addAttribute("cgpOptions", cgpList);

        List<Code> unitBizList = questionService.getAllType("UNIT_BIZ");
        model.addAttribute("unitOptions", unitBizList);

        return "ranking";
    }

    @PostMapping("/rank")
    public ResponseEntity<Page<RankResponse>> getPointsRank(
            @RequestBody RankRequest request
    ){
        return ResponseEntity.ok(myPageService.getPointsRankSearch(request));
    }

    @GetMapping("/rank/{eno}")
    public ResponseEntity<RankResponse> getMyPointsRank(
            @PathVariable String eno
    ){
        return ResponseEntity.ok(myPageService.getMyPointsRank(eno));
    }

    @GetMapping("/history/{eno}")
    public ResponseEntity<Page<RecentSolvedResponse>> getRecentSolved(
            @PathVariable String eno,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ){
        return ResponseEntity.ok(myPageService.getRecentSolved(eno, pageable));
    }

    @GetMapping("/statistic/{eno}")
    public ResponseEntity<List<SolvedStatisticResponse>> getSolvedStatistic(
            @PathVariable String eno
    ){
        return ResponseEntity.ok(myPageService.getSolvedStatistic(eno));
    }

    @GetMapping("/streak/{eno}")
    public ResponseEntity<List<StreakResponse>> getStreakYear(
            @PathVariable String eno
    ){
        return ResponseEntity.ok(myPageService.getStreakYear(eno));
    }

    // 포인트 이력
    @GetMapping("/mypage/point-history")
    public String getPointHistory(@PageableDefault(size = 20, page = 0) Pageable pageable,
                                  Model model, HttpSession session
    ) {
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String eno = loginResponse.getEno();

        Page<PointHistResponse> pointHist = myPageService.getPointHist(eno, pageable);

        model.addAttribute("pointHist", pointHist);

        return "point-history";
    }

    @GetMapping("/mypage/point-history-list")
    public String getPointHistoryList(@RequestParam(value = "page", defaultValue = "0") int page, Model model, HttpSession session) {

        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String eno = loginResponse.getEno();

        Pageable pageable = PageRequest.of(page, 20, Sort.by("seqId").descending());
        Page<PointHistoryEntity> poihtHistory = myPageService.findPointHistory(pageable, eno);
        model.addAttribute("pointHistory", poihtHistory);
        return "point-history-list";
    }
}
