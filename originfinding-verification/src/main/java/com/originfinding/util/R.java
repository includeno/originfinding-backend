package com.originfinding.util;

import com.originfinding.enums.HTTPCode;
import lombok.Data;

@Data
public class R {

    String message;
    Integer code;
    Object data;

    public static R build(Integer code,String message,Object data){
        R result=new R();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static R success(String message,Object data){
        R result=new R();
        result.setCode(HTTPCode.OK.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static R error(String message,Object data){
        R result=new R();
        result.setCode(HTTPCode.ERROR.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
