package com.lit.knowledgeforest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lit.knowledgeforest.entity.CategoryEntity;
import com.lit.knowledgeforest.entity.primarykey.CategoryPK;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, CategoryPK>{
}
