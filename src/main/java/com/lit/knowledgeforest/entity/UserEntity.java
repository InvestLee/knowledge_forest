package com.lit.knowledgeforest.entity;

import java.time.LocalDateTime;

import com.lit.knowledgeforest.entity.primarykey.UserPK;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "mbr_tb_inf")
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserEntity extends BaseEntity{

    @EmbeddedId
    private UserPK pk;

    @Column(name = "LD_EMP_C")
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

    @Column(name = "H_MI_DT_ST_YM")
    private String hMiDtStYm;

    @Column(name = "H_SUB_DT_ST_YM")
    private String hSubDtStYm;

    private String grpCd;

    private String bdLcYn;

    private String ipYn;

    private String cashbeeNo;

    private String elcAdd;

    private String lateType;
}

