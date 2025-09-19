package com.lit.knowledgeforest.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class BaseEntity {

    @CreatedBy
    @Column(name="FST_CRT_USID", updatable = false)
    protected String fstCrtUsid;

    @CreatedDate
    @Column(updatable = false,name="FST_CRT_DTTI")
    protected LocalDateTime fstCrtDtti;

    @LastModifiedBy
    @Column(name="LT_CH_USID")
    protected String ltChUsid;

    @LastModifiedDate
    @Column(name="LT_CH_DTTI")
    protected LocalDateTime ltChDtti;
}

