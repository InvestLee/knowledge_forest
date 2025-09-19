package com.lit.knowledgeforest.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import com.lit.knowledgeforest.dto.LoginResponse;

import jakarta.servlet.http.HttpSession;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Autowired
    private HttpSession session;

    @Override
    public Optional<String> getCurrentAuditor(){
        LoginResponse loginResponse = (LoginResponse) session.getAttribute("isLogin");
        return Optional.of(loginResponse.getEno());
    }
}
