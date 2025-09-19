package com.lit.knowledgeforest.service;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lit.knowledgeforest.common.SimpleSHA256;
import com.lit.knowledgeforest.dto.LoginRequest;
import com.lit.knowledgeforest.dto.LoginResponse;
import com.lit.knowledgeforest.entity.UserEntity;
import com.lit.knowledgeforest.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    //전체 순위
    @Transactional
    public LoginResponse doLogin (
            LoginRequest loginRequest,
            HttpServletRequest request
    ) throws Exception {
        // 복호화 처리
        HttpSession session = request.getSession();
        PrivateKey privateKey = (PrivateKey) session.getAttribute("__rsaPrivateKey__");
        session.removeAttribute("__rsaPrivateKey__"); // 키의 재사용을 막는다. 항상 새로운 키를 받도록 강제.

        String username = "", password = "", sha256Pswd = "";
        if (privateKey == null) {
            throw new RuntimeException("암호화 비밀키 정보를 찾을 수 없습니다.");
        }
        try {
            username = decryptRsa(privateKey, loginRequest.getEno());
            password = decryptRsa(privateKey, loginRequest.getPwd());
            // 비밀번호 암호화 방식 추가 (SHA256)
            sha256Pswd = SimpleSHA256.simpleDigest(password);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage(), ex);
        }
        UserEntity userEntity = userRepository.findByPkEno(username)
                .orElseThrow(() -> new EntityNotFoundException("유저 정보가 없습니다."));

        //패스워드 불일치
        if(!sha256Pswd.equals(userEntity.getPwd())){

            throw new Exception("패스워드 불일치합니다.");
        }

        //재직 여부
        if(userEntity.getPk().getVlYn().equals("N")){
            throw new Exception("재직 상태가 아닙니다.");
        }
        LoginResponse loginResponse = LoginResponse.builder()
                .eno(userEntity.getPk().getEno())
                .ldEmpC(userEntity.getLdEmpC())
                .ptNm(userEntity.getPtNm())
                .pwd(userEntity.getPwd())
                .empNm(userEntity.getEmpNm())
                .unitBiz(userEntity.getUnitBiz())
                .isAdmin(userEntity.getIsAdmin())
                .cgp(userEntity.getCgp())
                .cashbeeNo(userEntity.getCashbeeNo())
                .elcAdd(userEntity.getElcAdd())
                .isLogin(true)
                .build();
        request.getSession().setAttribute("isLogin", loginResponse);
        return loginResponse;
    }

    private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        if(securedValue == null) {
            throw new IllegalArgumentException("복호화 값 미존재");
        }
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] encryptedBytes = Base64.getDecoder().decode(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의
        return decryptedValue;
    }

    /**
     * 16진 문자열을 byte 배열로 변환한다.
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[]{};
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }

    /**
     * 로그인 여부 체크
     */
    public Boolean isLogin(HttpServletRequest request) {
        Object loginAttr = request.getSession().getAttribute("isLogin");
        return loginAttr instanceof LoginResponse && ((LoginResponse) loginAttr).getIsLogin();
    }

    /**
     * 로그아웃 처리
     */
    public Boolean doLogout(HttpServletRequest request) {
        request.getSession().removeAttribute("isLogin");
        return true;
    }

    /**
     * 초기 로그인 처리
     */
    public Boolean initialLogin(HttpServletRequest request) throws Exception{
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);

            KeyPair keyPair = generator.genKeyPair();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            HttpSession session = request.getSession();
            // 세션에 공개키의 문자열을 키로하여 개인키를 저장한다.
            session.setAttribute("__rsaPrivateKey__", privateKey);

            // 공개키를 문자열로 변환하여 JavaScript RSA 라이브러리 넘겨준다.
            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

            String publicKeyModulus = publicSpec.getModulus().toString(16);
            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

            request.setAttribute("publicKeyModulus", publicKeyModulus);
            request.setAttribute("publicKeyExponent", publicKeyExponent);

            /*************************************************************
             * 자동로그인 기능
             *************************************************************/
            String AUTO_LOGIN = "Y";
            if("Y".equals(AUTO_LOGIN)) {
                return true;
            }
            /*************************************************************
             * 자동로그인 off
             *************************************************************/
        } catch (Exception ex) {
            throw new ServletException(ex.getMessage(), ex);
        }

        return false;
    }
}
