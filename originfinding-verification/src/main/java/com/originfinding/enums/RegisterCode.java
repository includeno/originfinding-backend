package com.originfinding.enums;

public enum RegisterCode {
    USER_NAME_EXISTS(1,"用户名已被注册，无法注册成功"),
    EMAIL_EXISTS(2,"邮箱已被注册，无法注册成功"),

    OK(200,"成功"),
    DB_ERROR(500,"数据库错误"),
    ;

    Integer code;
    String message;
    RegisterCode(Integer code, String message){
        this.code=code;
        this.message=message;
    }
    public Integer getCode(){
        return code;
    }
    public String getMessage() {
        return message;
    }

    public static void main(String[] args) {
        System.out.println(RegisterCode.EMAIL_EXISTS.getCode());
    }
}
