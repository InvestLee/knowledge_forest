package com.lit.knowledgeforest.dto;

import com.lit.knowledgeforest.entity.CodeEntity;
import com.lit.knowledgeforest.entity.QuestionEntity;

import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Code {

    private String icVl;
    private String icVlcn;

    public Code(CodeEntity q) {
        this.icVl = q.getPk().getIcVl();
        this.icVlcn = q.getIcVlcn();
    }
}