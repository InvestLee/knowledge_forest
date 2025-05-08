package com.lit.knowledgeforest.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
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

    protected String fstCrtUsid;

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime fstCrtDtti;

    protected String ltChUsid;

    @LastModifiedDate
    protected LocalDateTime ltChDtti;
}
