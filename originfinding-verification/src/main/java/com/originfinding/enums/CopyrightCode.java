package com.originfinding.enums;

public enum CopyrightCode implements CodeInterface<Integer,String>{
    //通用
    DB_ERROR(-500,"数据库错误"),
    OK(200,"成功"),

    //requestId的状态 -1表示无关联 >0表示已关联
    NOT_RELEATED(-1,"未关联任何版权请求")
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
