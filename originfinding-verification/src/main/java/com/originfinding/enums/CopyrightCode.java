package com.originfinding.enums;

public enum CopyrightCode implements CodeInterface<Integer,String>{
    //通用
    OTHER_USER_EXIST(-400,"其他人已提交此url版权记录"),
    DB_ERROR(-500,"数据库错误"),
    OK(200,"成功"),

    ;

    final Integer code;
    final String message;
    CopyrightCode(Integer code, String message){
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
