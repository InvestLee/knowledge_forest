package com.lit.knowledgeforest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lit.knowledgeforest.dto.CategoryLevelRequest;
import com.lit.knowledgeforest.dto.CategoryQuestionRequest;
import com.lit.knowledgeforest.dto.Code;
import com.lit.knowledgeforest.dto.LoginResponse;
import com.lit.knowledgeforest.dto.Question;
import com.lit.knowledgeforest.dto.Quiz;
import com.lit.knowledgeforest.dto.QuizChoice;
import com.lit.knowledgeforest.dto.UserResponse;
import com.lit.knowledgeforest.service.HomePageService;
import com.lit.knowledgeforest.service.LoginService;
import com.lit.knowledgeforest.service.MyPageService;
import com.lit.knowledgeforest.service.QuestionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final LoginService loginService;
    private final HomePageService homePageService;
    private final MyPageService myPageService;

    @GetMapping("")
    public String getCategoryList(Model model, HttpSession session) {
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");

        //HomePageResponse result = homePageService.getHomePageInfo(loginResponse.getEno());
        UserResponse userResponse = myPageService.findUser(loginResponse.getEno());

        List<Code> categories = questionService.getAllQuizCategory();

        //model.addAttribute("homePageResponse", result);
        model.addAttribute("user", userResponse);
        model.addAttribute("categories", categories);

        return "categories";
    }

    @GetMapping("/category")
    public String getLevelList(
            @RequestParam("categoryId") String categoryId,
            Model model,
            HttpSession session
    ) {
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");

        //HomePageResponse result = homePageService.getHomePageInfo(loginResponse.getEno());
        UserResponse userResponse = myPageService.findUser(loginResponse.getEno());

        List<Code> levels = questionService.getAllQuizLevel();

        // model.addAttribute("homePageResponse", result);
        model.addAttribute("user", userResponse);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("levels", levels);

        return "levels";
    }

    @GetMapping("/category/level")
    public String getQuizList(
            @RequestParam("categoryId") String categoryId,
            @RequestParam("levelId") String levelId,
            @RequestParam(name="index", defaultValue = "0") int index,
            Model model,
            HttpSession session
    ) {
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");

        //HomePageResponse result = homePageService.getHomePageInfo(loginResponse.getEno());
        //UserResponse userResponse = myPageService.findUser(loginResponse.getEno());

        List<Quiz> quizzes = questionService.getCategoryQuestion(categoryId, levelId, loginResponse.getEno());

        Map<BigDecimal, List<QuizChoice>> choiceMap = new HashMap<>();
        for(Quiz quiz : quizzes) {
            if(quiz.getTypeCd().equals("3")) {
                choiceMap.put(
                        quiz.getQuestionNo(),
                        questionService.getChoiceQuestion(quiz.getQuestionNo())
                );
            }
        }

        //model.addAttribute("homePageResponse", result);
        //model.addAttribute("user", userResponse);
        model.addAttribute("quizzes", quizzes);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("levelId", levelId);
        model.addAttribute("index", index);
        model.addAttribute("choiceMap", choiceMap);
        return "quiz-list";
    }

    @PostMapping("/category/level/answer")
    public String submitAnswer(
            @RequestParam("categoryId") String categoryId,
            @RequestParam("levelId") String levelId,
            @RequestParam("index") int index,
            @RequestParam("userAnswer") String userAnswer,
            Model model,
            HttpSession session
    ) {
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");

        //HomePageResponse result = homePageService.getHomePageInfo(loginResponse.getEno());
        //UserResponse userResponse = myPageService.findUser(loginResponse.getEno());


        List<Quiz> quizzes = questionService.getCategoryQuestion(categoryId, levelId, loginResponse.getEno());

        boolean isCorrect = false;
        boolean hasNext = index + 1 < quizzes.size();

        if (!quizzes.isEmpty() && index < quizzes.size()) {
            Quiz current = quizzes.get(index);
            isCorrect = current.getAnswer().equalsIgnoreCase(userAnswer);

            questionService.solveQuiz(loginResponse.getEno(), current.getQuestionNo(), current.getPoints(), isCorrect);
        }

        //model.addAttribute("homePageResponse", result);
        //model.addAttribute("user", userResponse);
        model.addAttribute("quizzes", quizzes);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("levelId", levelId);
        model.addAttribute("index", index);
        model.addAttribute("isCorrect", isCorrect);
        model.addAttribute("hasNext", hasNext);

        return "quiz-list";
    }
}