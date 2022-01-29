package com.originfinding.enums;

public enum SpiderCode {

    SPIDER_COUNT_LIMIT(1),

    ;
    public int code;
    SpiderCode(int code){
        this.code=code;
    }
    public int getCode(){
        return this.code;
    }

}
