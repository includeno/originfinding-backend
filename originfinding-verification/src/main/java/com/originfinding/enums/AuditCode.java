package com.originfinding.enums;

public enum AuditCode implements CodeInterface<Integer,String>{

    OK(200,"成功"),
    AUTH_ERROR(-100,"权限不足"),
    RECORD_ERROR(-404,"不存在记录"),
    DB_ERROR(-500,"数据库错误"),
    ERROR(-1,"错误"),
    ;

    final Integer code;
    final String message;
    AuditCode(Integer code,String message){
        this.code=code;
        this.message=message;
    }
    @Override
    public Integer getCode(){
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}