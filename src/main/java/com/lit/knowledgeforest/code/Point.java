package com.lit.knowledgeforest.code;


public enum Point {

    POINT_0(0),
    POINT_10(10),
    POINT_30(30),
    POINT_50(50),
    POINT_70(70),
    POINT_100(100)
    ;

    private final int value;

    Point(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}