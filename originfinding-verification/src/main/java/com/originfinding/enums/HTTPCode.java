package com.originfinding.enums;

public enum HTTPCode {

    OK(200),
    NOT_FOUND(404),
    ERROR(-1),
    ;


    Integer code;
    HTTPCode(Integer code){
        this.code=code;
    }
    public Integer getCode(){
        return code;
    }
}
