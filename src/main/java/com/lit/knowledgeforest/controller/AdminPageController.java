package com.lit.knowledgeforest.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lit.knowledgeforest.code.CategoryName;
import com.lit.knowledgeforest.code.Level;
import com.lit.knowledgeforest.code.Point;
import com.lit.knowledgeforest.code.QuestionType;
import com.lit.knowledgeforest.code.Status;
import com.lit.knowledgeforest.dto.LoginResponse;
import com.lit.knowledgeforest.dto.Question;
import com.lit.knowledgeforest.entity.QuestionEntity;
import com.lit.knowledgeforest.repository.QuestionRepository;
import com.lit.knowledgeforest.service.AdminPageService;
import com.lit.knowledgeforest.service.WikiService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPageController {

    private final AdminPageService adminService;
    private final WikiService wikiService;
    private final QuestionRepository questionRepository;

    @GetMapping("")
    public String getAdminPage(Model model, HttpSession session) throws Exception {

        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String eno = loginResponse.getEno();

        model.addAttribute("isAdmin", adminService.isAdmin(eno));
        /*
         * if (!adminService.isAdmin(eno)) { throw new Exception("관리자가 아닙니다."); }
         */

        System.out.println("관리자 로그인 사번 :: " + eno);

        //Pageable pendingPageable = PageRequest.of(pendingPage, 10, Sort.by("fstCrtDtti").descending());
        //Pageable rejectedPageable = PageRequest.of(rejectedPage, 10, Sort.by("fstCrtDtti").descending());

        //model.addAttribute("pending", questionRepository.findByStatus(Status.PENDING.status(), pendingPageable));
        //model.addAttribute("rejected", questionRepository.findByStatus(Status.REJECT.status(), rejectedPageable));

        return "admin";
    }

    @GetMapping("/question/detail/{questionNo}")
    public String getQuestionDetail(@PathVariable("questionNo") BigDecimal questionNo, Model model, HttpSession session) throws Exception {
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String eno = loginResponse.getEno();

        if (!adminService.isAdmin(eno)) {
            throw new Exception("관리자가 아닙니다.");
        }

        Question question = wikiService.getQuestionDetails(questionNo);

        model.addAttribute("quiz", question);
        model.addAttribute("typeCdList", QuestionType.values());
        model.addAttribute("pointList", Point.values());
        model.addAttribute("categoryList", CategoryName.values());
        model.addAttribute("levelList", Level.values());

        return "admin/quiz-detail-modal :: modalContent";
    }

    @GetMapping("/question/rejected/detail/{questionNo}")
    public String getRejectedQuestionDetail(@PathVariable("questionNo") BigDecimal questionNo, Model model, HttpSession session) throws Exception {
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String eno = loginResponse.getEno();

        if (!adminService.isAdmin(eno)) {
            throw new Exception("관리자가 아닙니다.");
        }

        Question question = wikiService.getQuestionDetails(questionNo);

        model.addAttribute("quiz", question);
        model.addAttribute("typeCdList", QuestionType.values());
        model.addAttribute("pointList", Point.values());
        model.addAttribute("categoryList", CategoryName.values());
        model.addAttribute("levelList", Level.values());

        return "admin/quiz-detail-modal :: rejectedContent";
    }

    @GetMapping("/question/approved/detail/{questionNo}")
    public String getApprovedQuestionDetail(@PathVariable("questionNo") BigDecimal questionNo, Model model, HttpSession session) throws Exception {
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String eno = loginResponse.getEno();

        if (!adminService.isAdmin(eno)) {
            throw new Exception("관리자가 아닙니다.");
        }

        Question question = wikiService.getQuestionDetails(questionNo);

        model.addAttribute("quiz", question);
        model.addAttribute("typeCdList", QuestionType.values());
        model.addAttribute("pointList", Point.values());
        model.addAttribute("categoryList", CategoryName.values());
        model.addAttribute("levelList", Level.values());

        return "admin/quiz-detail-modal :: approvedContent";
    }


    // 승인 대기중 목록 페이징 (fragment 반환)
    @GetMapping("/pending")
    public String getPendingPage(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("questionNo").descending());
        Page<QuestionEntity> pending = adminService.findPending(pageable);
        model.addAttribute("pending", pending);
        return "admin/approve-pending";
    }

    // 반려 목록 페이징 (fragment 반환)
    @GetMapping("/rejected")
    public String getRejectedPage(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("questionNo").descending());
        Page<QuestionEntity> rejected = adminService.findRejected(pageable);
        model.addAttribute("rejected", rejected);
        return "admin/approve-rejected";
    }

    // 승인 목록 페이징 (fragment 반환)
    @GetMapping("/approved")
    public String getApprovedPage(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by("questionNo").descending());
        Page<QuestionEntity> approved = adminService.findApproved(pageable);
        model.addAttribute("approved", approved);
        return "admin/approve-approved";
    }

    // 문제승인
    @PostMapping("/approve")
    public ResponseEntity<Void> approveQuestion(@ModelAttribute Question q, HttpSession session){
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String userId = loginResponse.getEno();
        System.out.println("approve start");
        System.out.println(q);
        adminService.approveQuestion(q, userId);

        return ResponseEntity.ok().build();
    }

    // 문제반려
    @PostMapping("/reject")
    public ResponseEntity<Void> rejectQuestion(@ModelAttribute Question q, HttpSession session){
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String userId = loginResponse.getEno();
        System.out.println("reject start");
        adminService.rejectQuestion(q, userId);

        return ResponseEntity.ok().build();
    }

    // 문제수정
    @PostMapping("/update")
    public ResponseEntity<Void> updateQuestion(@ModelAttribute Question q, HttpSession session){
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String userId = loginResponse.getEno();
        System.out.println("update start");
        System.out.println(q);
        adminService.updateQuestion(q, userId);

        return ResponseEntity.ok().build();
    }

    // 문제삭제
    @PostMapping("/remove")
    public ResponseEntity<Void> removeQuestion(@ModelAttribute Question q, HttpSession session){
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        String userId = loginResponse.getEno();
        System.out.println("remove start");
        adminService.removeQuestion(q);

        return ResponseEntity.ok().build();
    }
}
