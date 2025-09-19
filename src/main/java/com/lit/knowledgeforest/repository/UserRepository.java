
package com.lit.knowledgeforest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lit.knowledgeforest.entity.UserEntity;
import com.lit.knowledgeforest.entity.primarykey.UserPK;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UserPK>{

    Optional<UserEntity> findByPkEno(String eno);
}
