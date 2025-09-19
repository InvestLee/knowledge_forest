package com.lit.knowledgeforest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@MapperScan(basePackages = "com.lit.knowledgeforest.mapper")
@EntityScan("com.lit.knowledgeforest.entity")
@EnableJpaAuditing
public class KnowledgeforestApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowledgeforestApplication.class, args);
	}

}
