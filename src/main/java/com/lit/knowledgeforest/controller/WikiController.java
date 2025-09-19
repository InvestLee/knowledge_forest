package com.lit.knowledgeforest.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lit.knowledgeforest.dto.QuestionList;
import com.lit.knowledgeforest.entity.QuestionEntity;
import com.lit.knowledgeforest.code.CategoryName;
import com.lit.knowledgeforest.code.Level;
import com.lit.knowledgeforest.code.Point;
import com.lit.knowledgeforest.code.QuestionType;
import com.lit.knowledgeforest.dto.LoginResponse;
import com.lit.knowledgeforest.dto.NewQuiz;
import com.lit.knowledgeforest.dto.Question;
import com.lit.knowledgeforest.service.WikiService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/wiki")
@RequiredArgsConstructor
public class WikiController {

    private final WikiService wikiService;

    // 문제 등록
    @PostMapping("/register")
    public ResponseEntity<Void> registerQuestion(@ModelAttribute NewQuiz q, HttpSession session){

        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String userId = loginResponse.getEno();
        System.out.println("퀴즈 등록");

        System.out.println(q);

        wikiService.createQuestion(q, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateQuestion(@RequestBody BigDecimal questionNo, @RequestBody Question qr, HttpSession session){

        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String userId = loginResponse.getEno();

        wikiService.upateQuestion(qr, userId);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/delete/{questionNo}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable BigDecimal questionNo){
        wikiService.deleteQuestion(questionNo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public String getQuestionList(){
        //public ResponseEntity<List<QuestionList>> getQuestionList(){
        //return ResponseEntity.ok(wikiService.getApprovedQuestions());
        return "wiki";

    }
 /*
 @GetMapping("/search")
 public ResponseEntity<List<QuestionList>> searchQuestionList(@RequestParam(required= false) BigDecimal questionNo, @RequestParam(required= false) String key){

 if(questionNo != null) {
 return ResponseEntity.ok(wikiService.getApprovedQuestionsByQuestionNo(questionNo));
 } else if(key != null) {
 return ResponseEntity.ok(wikiService.getApprovedQuestionsByContent(key));
 } else {
 return ResponseEntity.ok(null);
 }

 }
 */

 /*
 @GetMapping("/category")
 public ResponseEntity<List<QuestionList>> getQuestionCategoryList(@RequestParam String categoryName){
 return ResponseEntity.ok(wikiService.getApprovedQuestionsByCategory(categoryName));
 }
 */

    @GetMapping("/{questionNo}")
    public String getQuestionDetail(@PathVariable("questionNo") BigDecimal questionNo, Model model, HttpSession session){
        Question question = wikiService.getQuestionDetails(questionNo);

        model.addAttribute("quiz", question);
        return "wiki/wiki-detail-modal :: modalContent";
    }

    @GetMapping("/new")
    public String getNewPage(Model model, HttpSession session) {

        model.addAttribute("typeCdList", QuestionType.values());
        model.addAttribute("pointList", Point.values());
        model.addAttribute("categoryList", CategoryName.values());
        model.addAttribute("levelList", Level.values());
        return "new-quiz";
    }

    // 퀴즈목록 (fragment 반환)
    @GetMapping("/category")
    public String getQuestionCategoryList(@RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "category", defaultValue = "") String category,
                                          @RequestParam(value = "search", defaultValue = "") String search,
                                          Model model) {
        Pageable pageable = PageRequest.of(page, 15, Sort.by("questionNo").descending());
        Page<QuestionEntity> questions;
        if (category.equals("")) {
            // 전체 문제 조회
            if (!search.equals("")) {
                questions = wikiService.getApprovedQuestions(pageable, search);
            } else {
                questions = wikiService.getApprovedQuestions(pageable);
            }

        } else {
            // 카테고리별 조회
            if (!search.equals("")) {
                questions = wikiService.getApprovedQuestionsByCategory(pageable, category, search);
            } else {
                questions = wikiService.getApprovedQuestionsByCategory(pageable, category);
            }

        }
        model.addAttribute("questions", questions);
        return "wiki/wiki-list";
    }

}

