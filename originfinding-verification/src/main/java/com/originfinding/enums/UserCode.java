package com.originfinding.enums;

import lombok.Data;

public enum UserCode {
    USER_NAME_EXISTS(1),
    EMAIL_EXISTS(2),

    OK(200),
    DB_ERROR(500),
    ;


    public Integer code;

    UserCode(Integer code){
        this.code=code;
    }

    public Integer getCode(){
        return code;
    }

    public static void main(String[] args) {
        System.out.println(UserCode.EMAIL_EXISTS.getCode());
    }
}
