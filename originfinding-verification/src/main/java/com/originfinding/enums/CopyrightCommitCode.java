package com.originfinding.enums;

public enum CopyrightCommitCode implements CodeInterface<Integer,String>{
    //通用
    DB_ERROR(-500,"数据库错误"),
    OK(200,"成功"),
    REQUIRED_INFO_ERROR(-405,"未填写所有必填信息"),

    //提交
    URL_EXIST(-401,"链接已存在记录，请删除后提交"),

    //修改
    RECORD_ERROR(-404,"不存在记录"),
    AUTH_ERROR(-406,"修改权限错误"),
    ;

    final Integer code;
    final String message;
    CopyrightCommitCode(Integer code, String message){
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
