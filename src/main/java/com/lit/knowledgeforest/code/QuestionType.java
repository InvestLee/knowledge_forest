package com.lit.knowledgeforest.code;


public enum QuestionType {

    SHORT_ANSWER("1", "단답형"),
    OX("2", "OX형"),
    CHOICE("3", "객관식"),
    ;

    private final String code;
    private final String name;

    QuestionType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(String input) {
        for (QuestionType type : values()) {
            if (type.code.equals(input)) return type.name;
        }
        return "알 수 없음";
    }
}