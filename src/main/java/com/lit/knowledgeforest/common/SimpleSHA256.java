package com.lit.knowledgeforest.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimpleSHA256 {

    /**
     * SHA256 다이제스트 생성
     *
     * @param alg   SHA256 해시 알고리즘
     * @param input 암호화하려는 배열
     * @return 생성된 다이제스트 배열
     */
    public static byte[] digest(String alg, byte[] input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(alg);
        return md.digest(input);
    }

    /**
     * SHA256 암호화 기능
     *
     * @param inputValue 암호화하려는 문자열
     * @return 암호화된 문자열
     */
    public static String simpleDigest(String inputValue) throws Exception {
        if (inputValue == null)
            throw new Exception("Can't conver to String value!!");

        byte[] ret = digest("SHA-256", inputValue.getBytes());
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < ret.length; i++) {
            sb.append(Integer.toString((ret[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
