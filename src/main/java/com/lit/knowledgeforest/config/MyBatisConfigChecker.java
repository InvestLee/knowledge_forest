package com.lit.knowledgeforest.config;

import jakarta.annotation.PostConstruct;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MyBatisConfigChecker {

    private final SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    public void check() {
        Configuration configuration = sqlSessionFactory.getConfiguration();

        System.out.println("----- MyBatis Configuration Info -----");
        System.out.println("Map Underscore To Camel Case: " + configuration.isMapUnderscoreToCamelCase());
        System.out.println("Log Impl: " + configuration.getLogPrefix());
        System.out.println("Loaded Mappers: ");
        configuration.getMapperRegistry().getMappers().forEach(mapper -> {
            System.out.println(" Mapper: " + mapper.getName());
        });
        System.out.println("--------------------------------------");
    }
}