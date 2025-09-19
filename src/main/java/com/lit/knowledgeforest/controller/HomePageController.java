package com.lit.knowledgeforest.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import com.lit.knowledgeforest.dto.LoginResponse;
import com.lit.knowledgeforest.dto.RankResponse;
import com.lit.knowledgeforest.dto.RecentSolvedResponse;
import com.lit.knowledgeforest.service.HomePageService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final HomePageService homePageService;

    @GetMapping({"/", "/index"})
    public String initPage(HttpSession session, Model model) {
        List<RankResponse> topRank = homePageService.getFindTopPointRank(5);

        model.addAttribute("topRank", topRank);

        return "home";
    }

    @GetMapping("/home")
    public String getHomePageInfo(HttpSession session, Model model) {
        List<RankResponse> topRank = homePageService.getFindTopPointRank(5);

        model.addAttribute("topRank", topRank);

        return "home";
    }

    @GetMapping("/home/random-quiz")
    @ResponseBody
    public ResponseEntity<RecentSolvedResponse> getRandomQuiz(HttpSession session) {
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String eno = loginResponse.getEno();

        RecentSolvedResponse todayQuiz = homePageService.getTodayQuizByEno(eno);
        if(todayQuiz == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(todayQuiz);
    }

    @PostMapping("/home/submit-answer")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitAnswer(@RequestBody Map<String, String> request,
                                                            HttpSession session) {
        String eno = ((LoginResponse) session.getAttribute("isLogin")).getEno();
        BigDecimal questionNo = new BigDecimal(request.get("questionNo"));
        String userAnswer = request.get("answer");
        BigDecimal points = new BigDecimal(request.get("points"));

        Map<String, Object> response = homePageService.checkAnswer(eno, questionNo, userAnswer, points);

        return ResponseEntity.ok(response);
    }
}
