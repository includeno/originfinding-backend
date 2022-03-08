package com.originfinding.enums;

public enum UserCode implements CodeInterface<Integer,String>{

    OK(200,"成功"),
    DB_ERROR(-500,"数据库错误"),
    ERROR(-1,"错误"),

    UPDATE_DB_ERROR(-501,"更新数据库错误"),
    DELETE_DB_ERROR(-502,"删除数据库错误"),
    REQUIRED_INFO_ERROR(-405,"未填写所有必填信息"),
    ;

    final Integer code;
    final String message;
    UserCode(Integer code, String message){
        this.code=code;
        this.message=message;
    }
    public Integer getCode(){
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
