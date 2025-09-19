package com.lit.knowledgeforest.code;


public enum CategoryName {

    NETWORK("네트워크"),
    OS("운영체제"),
    ALGORITHM("알고리즘"),
    DB("데이터베이스"),
    CS("컴퓨터구조"),
    JAVA("Java"),
    IT("IT운영팀"),
    ;

    private final String name;

    CategoryName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}










