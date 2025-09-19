package com.lit.knowledgeforest.code;


public enum Level {

    EASY("1", "쉬움"),
    MEDIUN("2", "중간"),
    HARD("3", "어려움"),
    ;

    private final String code;
    private final String name;

    Level(String code, String name) {
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
        for (Level type : values()) {
            if (type.code.equals(input)) return type.name;
        }
        return "알 수 없음";
    }
}