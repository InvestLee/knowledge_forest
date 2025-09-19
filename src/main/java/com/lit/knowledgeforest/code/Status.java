package com.lit.knowledgeforest.code;


public enum Status {

    APPROVE("Y"),
    REJECT("N"),
    PENDING("P")
    ;

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
}