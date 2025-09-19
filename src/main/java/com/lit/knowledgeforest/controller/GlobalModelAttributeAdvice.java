package com.lit.knowledgeforest.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.lit.knowledgeforest.dto.HeaderInfo;
import com.lit.knowledgeforest.dto.LoginResponse;
import com.lit.knowledgeforest.service.CommonService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeAdvice {

    private final CommonService commService;

    @ModelAttribute("loginUser")
    public LoginResponse loginUser(HttpServletRequest request, HttpSession session) {
        String url = request.getRequestURI();
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        System.out.println("requestURI :" + url);
        if (loginResponse == null && url.startsWith("/login")) {
            System.out.println("loginUser 여기?");
            return null;
        }
        return loginResponse;
    }

    @ModelAttribute("userInfo")
    public HeaderInfo headerInfo(HttpServletRequest request, HttpSession session) {
        String url = request.getRequestURI();
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");

        if (loginResponse == null && url.startsWith("/login")) {
            System.out.println("userInfo 여기?");
            return null;
        }

        return commService.getHeaderInfo(loginResponse.getEno());
    }
}
