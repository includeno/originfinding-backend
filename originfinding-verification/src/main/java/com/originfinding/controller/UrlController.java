package com.originfinding.controller;

import com.originfinding.enums.HTTPCode;
import com.originfinding.util.R;
import com.originfinding.util.UrlFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

    //检测网页是否符合收录标准
    @GetMapping("/testurl")
    public R testUrl(String url){
        boolean res=false;
        try {
            res= UrlFilter.filter(url);
        } catch (NoSuchMethodException e) {

        }
        return R.build(HTTPCode.OK,res);
    }
}
