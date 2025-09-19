package com.lit.knowledgeforest.config;

import org.springframework.web.servlet.HandlerInterceptor;

import com.lit.knowledgeforest.dto.LoginResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("isLogin") == null) {
            if (uri.equals("/login")) {
                return true;
            }
            response.sendRedirect("/login");
            return false;
        }

        Object loginAttr = session.getAttribute("isLogin");

        if (!(loginAttr instanceof LoginResponse)) {
            response.sendRedirect("/login");
            return false;
        }

        LoginResponse loginResponse = (LoginResponse) loginAttr;

        if (!loginResponse.getIsLogin()) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}