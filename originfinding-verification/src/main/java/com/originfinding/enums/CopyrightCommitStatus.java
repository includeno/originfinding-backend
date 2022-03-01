package com.originfinding.enums;

public enum CopyrightCommitStatus implements CodeInterface<Integer,String>{
    NOT_AUDIT(0,"未审核状态"),
    PASSED(1,"已通过审核状态"),
    NOT_PASSED(-1,"未通过审核状态"),

    ;

    final Integer code;
    final String message;
    CopyrightCommitStatus(Integer code, String message){
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
