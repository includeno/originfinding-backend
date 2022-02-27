package com.originfinding.enums;

public enum UserStatus {

    DELETED(0),//已被删除
    OK(1),//正常
    ;

    public Integer code;

    UserStatus(Integer code){
        this.code=code;
    }

    public Integer getCode(){
        return code;
    }

}
