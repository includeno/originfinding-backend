package com.originfinding.enums;

public enum LoginCode {
    USER_DOESNT_EXIST(1,"用户名不存在"),
    EMAIL_DOESNT_EXIST(2,"邮箱不存在"),
    PASSWORD_ERROR(0,"密码错误"),

    DB_ERROR(500,"数据库错误"),

    OK(200,"成功"),
    ;

    Integer code;
    String message;
    LoginCode(Integer code, String message){
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
