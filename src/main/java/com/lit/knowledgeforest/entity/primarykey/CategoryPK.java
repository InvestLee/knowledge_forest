package com.lit.knowledgeforest.entity.primarykey;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class CategoryPK implements Serializable{

    BigDecimal questionNo;

    String categoryName;
}