package com.lit.knowledgeforest.entity;

import com.lit.knowledgeforest.entity.primarykey.CodePK;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "ic_tb_code")
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CodeEntity extends BaseEntity{

    @EmbeddedId
    private CodePK pk;

    private String icCn;

    private String icVlcn;
}



