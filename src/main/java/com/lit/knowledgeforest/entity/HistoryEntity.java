package com.lit.knowledgeforest.entity;

import com.lit.knowledgeforest.entity.primarykey.HistoryPK;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "qz_tb_history")
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HistoryEntity extends BaseEntity{

    @EmbeddedId
    private HistoryPK pk;

    private String correctYn;
}
