package com.lit.knowledgeforest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String eno;

    private String ldEmpC;

    private String ptNm;

    private String pwd;

    private String empNm;

    private String unitBiz;

    private String isAdmin;

    private String cgp;

    private String cashbeeNo;

    private String elcAdd;

    private Boolean isLogin;
}