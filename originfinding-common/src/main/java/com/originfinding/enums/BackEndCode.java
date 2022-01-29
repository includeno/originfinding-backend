package com.originfinding.enums;

public enum BackEndCode {

    SUCCESS(200),

    ;
    private int code;
    BackEndCode(int code){
        this.code=code;
    }
    public int getCode(){
        return this.code;
    }
}
