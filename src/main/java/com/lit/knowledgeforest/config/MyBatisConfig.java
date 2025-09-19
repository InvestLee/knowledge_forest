package com.lit.knowledgeforest.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;



@Configuration
@MapperScan(basePackages = "com.lit.knowledgeforest.mapper")
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) {
        try {
            SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
            sessionFactory.setDataSource(dataSource);
            sessionFactory.setMapperLocations(
                    new PathMatchingResourcePatternResolver()
                            .getResources("classpath:/mapper/**/*.xml")
            );

            org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
            configuration.setMapUnderscoreToCamelCase(true);
            configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
            sessionFactory.setConfiguration(configuration);

            return sessionFactory.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to create SqlSessionFactory", e);
        }
    }

}