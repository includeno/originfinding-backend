package com.originfinding.enums;

public enum CopyrightCode {
    //通用
    OTHER_USER_EXIST(400,"其他人已提交此url版权记录"),
    DB_ERROR(500,"数据库错误"),
    OK(200,"成功"),
    //审核

    //提交
    NOT_AUDIT(0,"变更为未审核状态"),
    PASSED(1,"已通过审核"),
    NOT_PASSED(-1,"未通过审核"),
    ;

    Integer code;
    String message;
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
