package com.originfinding.enums;

public enum RoleCode {

    User(1),
    Auditor(2),
    Admin(3),
    ;

    private Integer code;
    public Integer getCode(){
        return code;
    }

    RoleCode(Integer code) {
        this.code = code;
    }
}
