package com.lit.knowledgeforest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lit.knowledgeforest.entity.CodeEntity;
import com.lit.knowledgeforest.entity.primarykey.CodePK;

@Repository
public interface CodeRepository extends JpaRepository<CodeEntity, CodePK>{

    List<CodeEntity> findByPkIcC(String icC);
    CodeEntity findByPkIcVlAndIcVlcn(String icC, String icVlcn);
}


