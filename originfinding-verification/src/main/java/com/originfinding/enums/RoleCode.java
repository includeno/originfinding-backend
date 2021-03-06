package com.originfinding.enums;

public enum RoleCode {

    OK(200,"成功"),
    DB_ERROR(500,"数据库错误"),
    ;

    Integer code;
    String message;
    RoleCode(Integer code, String message){
        this.code=code;
        this.message=message;
    }
    public Integer getCode(){
        return code;
    }
    public String getMessage() {
        return message;
    }
}
