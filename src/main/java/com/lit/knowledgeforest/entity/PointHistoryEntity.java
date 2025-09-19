package com.lit.knowledgeforest.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "qz_tb_point_hist")
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@SuperBuilder
public class PointHistoryEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qz_tb_point_hist_seq_gen")
    @SequenceGenerator(name = "qz_tb_point_hist_seq_gen", sequenceName="qz_tb_point_hist_seq", allocationSize=1)
    private Long seqId;

    private String eno;

    private BigDecimal points;

    private String memo;
}


