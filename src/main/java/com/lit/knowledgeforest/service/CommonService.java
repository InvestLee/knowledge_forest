package com.lit.knowledgeforest.service;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.lit.knowledgeforest.dto.HeaderInfo;
import com.lit.knowledgeforest.mapper.UserInfoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonService {
    private final UserInfoMapper userInfoMapper;

    public HeaderInfo getHeaderInfo(String eno) {

        HashMap<String, Object> headerInfo = userInfoMapper.findHeaderInfoByEno(eno);

        return HeaderInfo.builder()
                .eno((String) headerInfo.get("ENO"))
                .name((String) headerInfo.get("EMP_NM"))
                .points((BigDecimal) headerInfo.get("POINTS"))
                .build();
    }
}

