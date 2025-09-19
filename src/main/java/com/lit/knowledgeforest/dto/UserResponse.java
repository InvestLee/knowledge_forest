package com.lit.knowledgeforest.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String eno;

    private String vlYn;

    private String ldEmpC;

    private String bd;

    private String jgdNm;

    private String ptNm;

    private String pwd;

    private String empNm;

    private String unitBiz;

    private String acno;

    private String bnkNm;

    private String subDtStYm;

    private String miDtStYm;

    private String isAdmin;

    private String cgp;

    private LocalDateTime vald;

    private String hMiDtStYm;

    private String hSubDtStYm;

    private String grpCd;

    private String bdLcYn;

    private String ipYn;

    private String cashbeeNo;

    private String elcAdd;

    private String lateType;
}
