package com.lit.knowledgeforest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lit.knowledgeforest.dto.RankResponse;
import com.lit.knowledgeforest.dto.RecentSolvedResponse;
import com.lit.knowledgeforest.service.MyPageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/rank")
    public ResponseEntity<List<RankResponse>> getPointsRank(){
        return ResponseEntity.ok(myPageService.getPointsRank());
    }

    @GetMapping("/rank/{eno}")
    public ResponseEntity<RankResponse> getMyPointsRank(@PathVariable String eno){
        return ResponseEntity.ok(myPageService.getMyPointsRank(eno));
    }

    @GetMapping("/history/{eno}")
    public ResponseEntity<List<RecentSolvedResponse>> getRecentSolved(@PathVariable String eno){
        return ResponseEntity.ok(myPageService.getRecentSolved(eno));
    }
}
