package com.lit.knowledgeforest.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lit.knowledgeforest.dto.LoginRequest;
import com.lit.knowledgeforest.dto.LoginResponse;
import com.lit.knowledgeforest.service.LoginService;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // 로그인 페이지 진입 시 RSA 키 초기화
    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) throws Exception {
        System.out.println("getmapping login");

        LoginResponse loginResponse =  (LoginResponse) request.getSession().getAttribute("isLogin");

        // 로그인 되어 있으면 홈화면으로
        if (loginResponse != null) {
            return "redirect:/home";
        }

        model.addAttribute("loginRequest", new LoginRequest());
        boolean isAutoLogin = loginService.initialLogin(request);
        if(isAutoLogin) {
            model.addAttribute("eno", request.getParameter("id"));
            model.addAttribute("pwd", request.getParameter("pw"));
        }
        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String loginProcess(@ModelAttribute LoginRequest loginRequest,
                               @RequestParam(name="returnUrl", required = false, defaultValue = "/home") String returnUrl,
                               HttpServletRequest request,
                               Model model,
                               RedirectAttributes redirectAttributes) throws Exception {

        try {
            System.out.println("postmapping login");
            LoginResponse loginResponse =  loginService.doLogin(loginRequest, request);
            return "redirect:" + returnUrl;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login?error=1";
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        loginService.doLogout(request);
        return "redirect:/login";
    }
}
