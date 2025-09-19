package com.lit.knowledgeforest.config;

import java.io.InputStream;

import jakarta.annotation.PostConstruct;

import org.apache.ibatis.io.Resources;
import org.springframework.stereotype.Component;

@Component
public class MapperXmlLoaderCheck {

    @PostConstruct
    public void check() throws Exception {
        String resource = "mapper/UserInfoMapper.xml";

        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            if (inputStream != null) {
                System.out.println(resource + " is loaded successfully.");
            } else {
                System.out.println(resource + " is NOT found.");
            }
        } catch (Exception e) {
            System.out.println("Exception while loading " + resource);
            e.printStackTrace();
        }
    }
}